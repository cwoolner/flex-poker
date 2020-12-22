package com.flexpoker.game.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.BlindsIncreasedEvent
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameFinishedEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import com.flexpoker.game.command.events.TableRemovedEvent
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import com.flexpoker.game.query.dto.GameStage
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.PMap
import org.pcollections.PSet
import java.util.ArrayList
import java.util.HashMap
import java.util.Optional
import java.util.UUID

class Game constructor(
    creatingFromEvents: Boolean,
    aggregateId: UUID,
    gameName: String,
    maxNumberOfPlayers: Int,
    numberOfPlayersPerTable: Int,
    numberOfSecondsForActionOnTimer: Int,
    createdById: UUID,
    numberOfMinutesBetweenBlindLevels: Int
) {
    private var state = GameState(
        aggregateId,
        maxNumberOfPlayers,
        numberOfPlayersPerTable,
        GameStage.REGISTERING,
        blindSchedule(numberOfMinutesBetweenBlindLevels)
    )

    private val newEvents: MutableList<GameEvent>
    private val appliedEvents: MutableList<GameEvent>

    fun fetchNewEvents(): List<GameEvent> {
        return ArrayList(newEvents)
    }

    fun fetchAppliedEvents(): List<GameEvent> {
        return ArrayList(appliedEvents)
    }

    fun applyAllHistoricalEvents(events: List<GameEvent>) {
        events.forEach { applyCommonEvent(it) }
    }

    private fun applyCommonEvent(event: GameEvent) {
        when(event) {
            is GameCreatedEvent -> { }
            is GameJoinedEvent ->
                state = state.copy(registeredPlayerIds = state.registeredPlayerIds.plus(event.playerId))
            is GameMovedToStartingStageEvent -> state = state.copy(stage = GameStage.STARTING)
            is GameStartedEvent -> state = state.copy(stage = GameStage.INPROGRESS)
            is GameTablesCreatedAndPlayersAssociatedEvent ->
                state = state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plusAll(event.tableIdToPlayerIdsMap))
            is GameFinishedEvent -> state = state.copy(stage = GameStage.FINISHED)
            is NewHandIsClearedToStartEvent -> { }
            is BlindsIncreasedEvent -> state = incrementLevel(state)
            is TableRemovedEvent -> {
                state = state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.minus(event.tableId))
                state = state.copy(pausedTablesForBalancing = state.pausedTablesForBalancing.minus(event.tableId))
            }
            is TablePausedForBalancingEvent ->
                state = state.copy(pausedTablesForBalancing = state.pausedTablesForBalancing.plus(event.tableId))
            is TableResumedAfterBalancingEvent ->
                state = state.copy(pausedTablesForBalancing = state.pausedTablesForBalancing.minus(event.tableId))
            is PlayerMovedToNewTableEvent -> {
                val fromPlayerIds = state.tableIdToPlayerIdsMap[event.fromTableId]!!.minus(event.playerId)
                val toPlayerIds = state.tableIdToPlayerIdsMap[event.fromTableId]!!.plus(event.playerId)
                state = state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plus(event.fromTableId, fromPlayerIds))
                state = state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plus(event.toTableId, toPlayerIds))
            }
            is PlayerBustedGameEvent -> {
                val tableId = state.tableIdToPlayerIdsMap.entries.first { it.value.contains(event.playerId) }.key
                val playerIds = state.tableIdToPlayerIdsMap[tableId]!!.minus(event.playerId)
                state = state.copy(tableIdToPlayerIdsMap = state.tableIdToPlayerIdsMap.plus(tableId, playerIds))
            }
        }
        appliedEvents.add(event)
    }

    fun joinGame(playerId: UUID) {
        createJoinGameEvent(playerId)

        // if the game is at the max capacity, start the game
        if (state.registeredPlayerIds.size == state.maxNumberOfPlayers) {
            createGameMovedToStartingStageEvent()
            createGameTablesCreatedAndPlayersAssociatedEvent()
            createGameStartedEvent()
        }
    }

    fun attemptToStartNewHand(tableId: UUID, playerToChipsAtTableMap: Map<UUID, Int>) {
        if (state.stage !== GameStage.INPROGRESS) {
            throw FlexPokerException("the game must be INPROGRESS if trying to start a new hand")
        }
        playerToChipsAtTableMap.filterValues { it == 0 }.map { it.key }.forEach { bustPlayer(it) }

        val bustedPlayers = state.tableIdToPlayerIdsMap[tableId]!!
            .filter { !playerToChipsAtTableMap.keys.contains(it) }.toSet()
        bustedPlayers.forEach { bustPlayer(it) }

        if (state.tableIdToPlayerIdsMap.flatMap { it.value }.count() == 1) {
            // TODO: do something for the winner
        } else {
            var singleBalancingEvent: Optional<GameEvent>?
            do {
                singleBalancingEvent = createSingleBalancingEvent(state.aggregateId, state.maxNumberOfPlayers, tableId,
                    state.pausedTablesForBalancing, state.tableIdToPlayerIdsMap, playerToChipsAtTableMap)
                if (singleBalancingEvent.isPresent) {
                    newEvents.add(singleBalancingEvent.get())
                    applyCommonEvent(singleBalancingEvent.get())
                }
            } while (singleBalancingEvent!!.isPresent)
            if (state.tableIdToPlayerIdsMap.containsKey(tableId) && !state.pausedTablesForBalancing.contains(tableId)) {
                val event = NewHandIsClearedToStartEvent(state.aggregateId, tableId, currentBlindAmounts(state))
                newEvents.add(event)
                applyCommonEvent(event)
            }
        }
    }

    fun increaseBlinds() {
        if (state.stage !== GameStage.INPROGRESS) {
            throw FlexPokerException("cannot increase blinds if the game isn't in progress")
        }
        if (!isMaxLevel(state)) {
            val event = BlindsIncreasedEvent(state.aggregateId)
            newEvents.add(event)
            applyCommonEvent(event)
        }
    }

    private fun bustPlayer(playerId: UUID) {
        if (state.tableIdToPlayerIdsMap.flatMap { it.value }.none { it == playerId }) {
            throw FlexPokerException("player is not active in the game")
        }
        val event = PlayerBustedGameEvent(state.aggregateId, playerId)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createJoinGameEvent(playerId: UUID) {
        if (state.stage === GameStage.STARTING || state.stage === GameStage.INPROGRESS) {
            throw FlexPokerException("The game has already started")
        }
        if (state.stage === GameStage.FINISHED) {
            throw FlexPokerException("The game is already finished.")
        }
        if (state.registeredPlayerIds.contains(playerId)) {
            throw FlexPokerException("You are already in this game.")
        }
        val event = GameJoinedEvent(state.aggregateId, playerId)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createGameMovedToStartingStageEvent() {
        if (state.stage !== GameStage.REGISTERING) {
            throw FlexPokerException("to move to STARTING, the game stage must be REGISTERING")
        }
        val event = GameMovedToStartingStageEvent(state.aggregateId)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createGameTablesCreatedAndPlayersAssociatedEvent() {
        if (state.tableIdToPlayerIdsMap.isNotEmpty()) {
            throw FlexPokerException("tableToPlayerIdsMap should be empty when initializing the tables")
        }
        val tableMap = createTableToPlayerMap()
        val event = GameTablesCreatedAndPlayersAssociatedEvent(state.aggregateId, tableMap, state.numberOfPlayersPerTable)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createTableToPlayerMap(): PMap<UUID, PSet<UUID>> {
        val randomizedListOfPlayerIds = state.registeredPlayerIds.toList().shuffled()
        val numberOfTablesToCreate = determineNumberOfTablesToCreate()

        val tableMap = (0 until numberOfTablesToCreate)
            .fold(HashMap<UUID, PSet<UUID>>(), { acc, _ ->
                acc[UUID.randomUUID()] = HashTreePSet.empty()
                acc
            })

        val tableIdList = ArrayList(tableMap.keys)
        randomizedListOfPlayerIds.indices.forEach {
            val tableIndex = it % tableIdList.size
            val players = tableMap[tableIdList[tableIndex]]!!.plus(randomizedListOfPlayerIds[it])
            tableMap[tableIdList[tableIndex]] = players
        }

        return HashTreePMap.from(tableMap)
    }

    private fun determineNumberOfTablesToCreate(): Int {
        var numberOfTables = state.registeredPlayerIds.size / state.numberOfPlayersPerTable

        // if the number of people doesn't fit perfectly, then an additional
        // table is needed for the overflow
        if (state.registeredPlayerIds.size % state.numberOfPlayersPerTable != 0) {
            numberOfTables++
        }
        return numberOfTables
    }

    private fun createGameStartedEvent() {
        if (state.stage !== GameStage.STARTING) {
            throw FlexPokerException("to move to STARTED, the game stage must be STARTING")
        }
        if (state.tableIdToPlayerIdsMap.isEmpty()) {
            throw FlexPokerException("tableToPlayerIdsMap should be filled at this point")
        }
        val event = GameStartedEvent(state.aggregateId, state.tableIdToPlayerIdsMap.keys, state.blindScheduleDTO)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    init {
        newEvents = ArrayList()
        appliedEvents = ArrayList()

        if (!creatingFromEvents) {
            val gameCreatedEvent = GameCreatedEvent(
                aggregateId, gameName, maxNumberOfPlayers,
                state.numberOfPlayersPerTable, createdById, numberOfMinutesBetweenLevels(state),
                numberOfSecondsForActionOnTimer
            )
            newEvents.add(gameCreatedEvent)
            applyCommonEvent(gameCreatedEvent)
        }
    }
}