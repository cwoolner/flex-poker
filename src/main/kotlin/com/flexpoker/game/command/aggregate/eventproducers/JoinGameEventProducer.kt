package com.flexpoker.game.command.aggregate.eventproducers

import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.applyEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.GameStage
import com.flexpoker.util.toPMap
import org.pcollections.HashTreePSet
import org.pcollections.PMap
import org.pcollections.PSet
import java.util.UUID

fun joinGame(state: GameState, playerId: UUID): List<GameEvent> {
    val events = mutableListOf<GameEvent>()

    val gameCreatedEvent = createJoinGameEvent(state, playerId)
    events.add(gameCreatedEvent)
    var updatedState = applyEvent(state, gameCreatedEvent)

    // if the game is at the max capacity, start the game
    if (updatedState.registeredPlayerIds.size == updatedState.maxNumberOfPlayers) {
        val event2 = createGameMovedToStartingStageEvent(updatedState)
        events.add(event2)
        updatedState = applyEvent(updatedState, event2)

        val event3 = createGameTablesCreatedAndPlayersAssociatedEvent(updatedState)
        events.add(event3)
        updatedState = applyEvent(updatedState, event3)

        val event4 = createGameStartedEvent(updatedState)
        events.add(event4)
    }
    return events
}

private fun createJoinGameEvent(state: GameState, playerId: UUID): GameEvent {
    require(state.stage === GameStage.REGISTERING) { "game must be in registering stage" }
    require(!state.registeredPlayerIds.contains(playerId)) { "You are already in this game." }
    return GameJoinedEvent(state.aggregateId, playerId)
}

private fun createGameMovedToStartingStageEvent(state: GameState): GameMovedToStartingStageEvent {
    require (state.stage === GameStage.REGISTERING) { "to move to STARTING, the game stage must be REGISTERING" }
    return GameMovedToStartingStageEvent(state.aggregateId)
}

private fun createGameTablesCreatedAndPlayersAssociatedEvent(state: GameState): GameTablesCreatedAndPlayersAssociatedEvent {
    require (state.tableIdToPlayerIdsMap.isEmpty()) { "tableToPlayerIdsMap should be empty when initializing the tables" }
    val tableMap = createTableToPlayerMap(state)
    return GameTablesCreatedAndPlayersAssociatedEvent(state.aggregateId, tableMap, state.numberOfPlayersPerTable)
}

private fun createGameStartedEvent(state: GameState): GameStartedEvent {
    require(state.stage === GameStage.STARTING) { "to move to STARTED, the game stage must be STARTING" }
    require (state.tableIdToPlayerIdsMap.isNotEmpty()) { "tableToPlayerIdsMap should be filled at this point" }
    return GameStartedEvent(state.aggregateId, state.tableIdToPlayerIdsMap.keys, state.blindScheduleDTO)
}

private fun createTableToPlayerMap(state: GameState): PMap<UUID, PSet<UUID>> {
    val randomizedListOfPlayerIds = state.registeredPlayerIds.toList().shuffled()
    val numberOfTablesToCreate = determineNumberOfTablesToCreate(state)

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

    return tableMap.toPMap()
}

private fun determineNumberOfTablesToCreate(state: GameState): Int {
    var numberOfTables = state.registeredPlayerIds.size / state.numberOfPlayersPerTable

    // if the number of people doesn't fit perfectly, then an additional
    // table is needed for the overflow
    if (state.registeredPlayerIds.size % state.numberOfPlayersPerTable != 0) {
        numberOfTables++
    }
    return numberOfTables
}

