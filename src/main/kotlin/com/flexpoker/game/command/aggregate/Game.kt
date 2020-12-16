package com.flexpoker.game.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.framework.command.EventApplier
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
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.Optional
import java.util.UUID

class Game constructor(
    creatingFromEvents: Boolean,
    private val aggregateId: UUID,
    gameName: String,
    private val maxNumberOfPlayers: Int,
    private val numberOfPlayersPerTable: Int,
    numberOfSecondsForActionOnTimer: Int,
    createdById: UUID,
    private var gameStage: GameStage,
    private val blindSchedule: BlindSchedule,
    private val tableBalancer: TableBalancer
) {
    private val newEvents: MutableList<GameEvent>
    private val appliedEvents: MutableList<GameEvent>
    private val methodTable: MutableMap<Class<out GameEvent>, EventApplier<in GameEvent>>
    private val registeredPlayerIds: MutableSet<UUID?>
    private val tableIdToPlayerIdsMap: MutableMap<UUID, MutableSet<UUID>>
    private val pausedTablesForBalancing: MutableSet<UUID>

    fun fetchNewEvents(): List<GameEvent> {
        return ArrayList(newEvents)
    }

    fun fetchAppliedEvents(): List<GameEvent> {
        return ArrayList(appliedEvents)
    }

    fun applyAllHistoricalEvents(events: List<GameEvent>) {
        events.forEach { applyCommonEvent(it) }
    }

    private fun populateMethodTable() {
        methodTable[GameCreatedEvent::class.java] = EventApplier { }
        methodTable[GameJoinedEvent::class.java] = EventApplier { registeredPlayerIds.add((it as GameJoinedEvent).playerId) }
        methodTable[GameMovedToStartingStageEvent::class.java] = EventApplier { gameStage = GameStage.STARTING }
        methodTable[GameStartedEvent::class.java] = EventApplier { gameStage = GameStage.INPROGRESS }
        methodTable[GameTablesCreatedAndPlayersAssociatedEvent::class.java] = EventApplier { x: GameEvent ->
            tableIdToPlayerIdsMap.putAll(
                (x as GameTablesCreatedAndPlayersAssociatedEvent)
                    .tableIdToPlayerIdsMap as Map<out UUID, MutableSet<UUID>>)
        }
        methodTable[GameFinishedEvent::class.java] = EventApplier {  gameStage = GameStage.FINISHED }
        methodTable[NewHandIsClearedToStartEvent::class.java] = EventApplier { }
        methodTable[BlindsIncreasedEvent::class.java] = EventApplier { blindSchedule.incrementLevel() }
        methodTable[TableRemovedEvent::class.java] = EventApplier {
            val tableId = (it as TableRemovedEvent).tableId
            tableIdToPlayerIdsMap.remove(tableId)
            pausedTablesForBalancing.remove(tableId)
        }
        methodTable[TablePausedForBalancingEvent::class.java] = EventApplier {
            val tableId = (it as TablePausedForBalancingEvent).tableId
            pausedTablesForBalancing.add(tableId)
        }
        methodTable[TableResumedAfterBalancingEvent::class.java] = EventApplier {
            val tableId = (it as TableResumedAfterBalancingEvent).tableId
            pausedTablesForBalancing.remove(tableId)
        }
        methodTable[PlayerMovedToNewTableEvent::class.java] = EventApplier {
            val event = it as PlayerMovedToNewTableEvent
            tableIdToPlayerIdsMap[event.fromTableId]!!.remove(event.playerId)
            tableIdToPlayerIdsMap[event.toTableId]!!.add(event.playerId)
        }
        methodTable[PlayerBustedGameEvent::class.java] = EventApplier { x: GameEvent ->
            val event = x as PlayerBustedGameEvent
            val tableId = tableIdToPlayerIdsMap.entries.first { it.value.contains(event.playerId) }.key
            tableIdToPlayerIdsMap[tableId]!!.remove(event.playerId)
        }
    }

    private fun applyCommonEvent(event: GameEvent) {
        methodTable[event.javaClass]!!.applyEvent(event)
        appliedEvents.add(event)
    }

    fun joinGame(playerId: UUID) {
        createJoinGameEvent(playerId)

        // if the game is at the max capacity, start the game
        if (registeredPlayerIds.size == maxNumberOfPlayers) {
            createGameMovedToStartingStageEvent()
            createGameTablesCreatedAndPlayersAssociatedEvent()
            createGameStartedEvent()
        }
    }

    fun attemptToStartNewHand(tableId: UUID, playerToChipsAtTableMap: Map<UUID, Int>) {
        if (gameStage !== GameStage.INPROGRESS) {
            throw FlexPokerException("the game must be INPROGRESS if trying to start a new hand")
        }
        playerToChipsAtTableMap.filterValues { it == 0 }.map { it.key }.forEach { bustPlayer(it) }

        val bustedPlayers = tableIdToPlayerIdsMap[tableId]!!
            .filter { !playerToChipsAtTableMap.keys.contains(it) }.toSet()
        bustedPlayers.forEach { bustPlayer(it) }

        if (tableIdToPlayerIdsMap.flatMap { it.value }.count() == 1) {
            // TODO: do something for the winner
        } else {
            var singleBalancingEvent: Optional<GameEvent>?
            do {
                singleBalancingEvent = tableBalancer.createSingleBalancingEvent(tableId, pausedTablesForBalancing,
                    tableIdToPlayerIdsMap, playerToChipsAtTableMap)
                if (singleBalancingEvent.isPresent) {
                    newEvents.add(singleBalancingEvent.get())
                    applyCommonEvent(singleBalancingEvent.get())
                }
            } while (singleBalancingEvent!!.isPresent)
            if (tableIdToPlayerIdsMap.containsKey(tableId) && !pausedTablesForBalancing.contains(tableId)) {
                val event = NewHandIsClearedToStartEvent(aggregateId, tableId, blindSchedule.currentBlindAmounts)
                newEvents.add(event)
                applyCommonEvent(event)
            }
        }
    }

    fun increaseBlinds() {
        if (gameStage !== GameStage.INPROGRESS) {
            throw FlexPokerException("cannot increase blinds if the game isn't in progress")
        }
        if (!blindSchedule.isMaxLevel) {
            val event = BlindsIncreasedEvent(aggregateId)
            newEvents.add(event)
            applyCommonEvent(event)
        }
    }

    private fun bustPlayer(playerId: UUID) {
        if (tableIdToPlayerIdsMap.flatMap { it.value }.none { it == playerId }) {
            throw FlexPokerException("player is not active in the game")
        }
        val event = PlayerBustedGameEvent(aggregateId, playerId)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createJoinGameEvent(playerId: UUID) {
        if (gameStage === GameStage.STARTING || gameStage === GameStage.INPROGRESS) {
            throw FlexPokerException("The game has already started")
        }
        if (gameStage === GameStage.FINISHED) {
            throw FlexPokerException("The game is already finished.")
        }
        if (registeredPlayerIds.contains(playerId)) {
            throw FlexPokerException("You are already in this game.")
        }
        val event = GameJoinedEvent(aggregateId, playerId)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createGameMovedToStartingStageEvent() {
        if (gameStage !== GameStage.REGISTERING) {
            throw FlexPokerException("to move to STARTING, the game stage must be REGISTERING")
        }
        val event = GameMovedToStartingStageEvent(aggregateId)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createGameTablesCreatedAndPlayersAssociatedEvent() {
        if (tableIdToPlayerIdsMap.isNotEmpty()) {
            throw FlexPokerException("tableToPlayerIdsMap should be empty when initializing the tables")
        }
        val tableMap = createTableToPlayerMap()
        val event = GameTablesCreatedAndPlayersAssociatedEvent(aggregateId, tableMap, numberOfPlayersPerTable)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    private fun createTableToPlayerMap(): Map<UUID, Set<UUID>> {
        val randomizedListOfPlayerIds = ArrayList(registeredPlayerIds)
        Collections.shuffle(randomizedListOfPlayerIds)
        val tableMap = HashMap<UUID, MutableSet<UUID?>>()
        val numberOfTablesToCreate = determineNumberOfTablesToCreate()

        (0 until numberOfTablesToCreate).forEach { _ -> tableMap[UUID.randomUUID()] = HashSet() }

        val tableIdList = ArrayList(tableMap.keys)
        (0 until randomizedListOfPlayerIds.size).forEach {
            val tableIndex = it % tableIdList.size
            tableMap[tableIdList[tableIndex]]!!.add(randomizedListOfPlayerIds[it])
        }

        return tableMap as Map<UUID, Set<UUID>>
    }

    private fun determineNumberOfTablesToCreate(): Int {
        var numberOfTables = registeredPlayerIds.size / numberOfPlayersPerTable

        // if the number of people doesn't fit perfectly, then an additional
        // table is needed for the overflow
        if (registeredPlayerIds.size % numberOfPlayersPerTable != 0) {
            numberOfTables++
        }
        return numberOfTables
    }

    private fun createGameStartedEvent() {
        if (gameStage !== GameStage.STARTING) {
            throw FlexPokerException("to move to STARTED, the game stage must be STARTING")
        }
        if (tableIdToPlayerIdsMap.isEmpty()) {
            throw FlexPokerException("tableToPlayerIdsMap should be filled at this point")
        }
        val event = GameStartedEvent(aggregateId, tableIdToPlayerIdsMap.keys, blindSchedule.blindScheduleDTO)
        newEvents.add(event)
        applyCommonEvent(event)
    }

    init {
        registeredPlayerIds = HashSet()
        tableIdToPlayerIdsMap = HashMap()
        pausedTablesForBalancing = HashSet()
        newEvents = ArrayList()
        appliedEvents = ArrayList()
        methodTable = HashMap()
        populateMethodTable()
        if (!creatingFromEvents) {
            val gameCreatedEvent = GameCreatedEvent(
                aggregateId, gameName, maxNumberOfPlayers,
                numberOfPlayersPerTable, createdById, blindSchedule.numberOfMinutesBetweenLevels,
                numberOfSecondsForActionOnTimer
            )
            newEvents.add(gameCreatedEvent)
            applyCommonEvent(gameCreatedEvent)
        }
    }
}