package com.flexpoker.game.command.aggregate.eventproducers

import com.flexpoker.game.command.GameStage
import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.applyEvent
import com.flexpoker.game.command.aggregate.applyEvents
import com.flexpoker.game.command.aggregate.createSingleBalancingEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import java.util.Optional
import java.util.UUID

fun attemptToStartNewHand(state: GameState, tableId: UUID, playerToChipsAtTableMap: Map<UUID, Int>): List<GameEvent> {
    require(state.stage === GameStage.INPROGRESS) { "the game must be INPROGRESS if trying to start a new hand" }

    val events = mutableListOf<GameEvent>()

    var (updatedState, bustedEvents) = bustedEvents(state, tableId, playerToChipsAtTableMap)
    events.addAll(bustedEvents)

    if (updatedState.tableIdToPlayerIdsMap.flatMap { it.value }.count() == 1) {
        // TODO: do something for the winner
    } else {
        var singleBalancingEvent: Optional<GameEvent>?
        do {
            singleBalancingEvent = createSingleBalancingEvent(updatedState.aggregateId, updatedState.numberOfPlayersPerTable, tableId,
                updatedState.pausedTablesForBalancing, updatedState.tableIdToPlayerIdsMap, playerToChipsAtTableMap)
            if (singleBalancingEvent.isPresent) {
                events.add(singleBalancingEvent.get())
                updatedState = applyEvent(updatedState, singleBalancingEvent.get())
            }
        } while (singleBalancingEvent!!.isPresent)

        if (updatedState.tableIdToPlayerIdsMap.containsKey(tableId) && !updatedState.pausedTablesForBalancing.contains(tableId)) {
            val event = NewHandIsClearedToStartEvent(updatedState.aggregateId, tableId, updatedState.blindSchedule.currentBlinds())
            events.add(event)
            updatedState = applyEvent(updatedState, event)
        }
    }

    return events
}

private fun bustedEvents(state: GameState, tableId: UUID, playerToChipsAtTableMap: Map<UUID, Int>): Pair<GameState, List<GameEvent>> {
    val events = mutableListOf<GameEvent>()

    val noChipsBustedEvents = playerToChipsAtTableMap.filterValues { it == 0 }.map { it.key }.map { bustPlayer(state, it) }
    events.addAll(noChipsBustedEvents)
    var updatedState = applyEvents(state, noChipsBustedEvents)

    val inGameButMissingFromTablePlayers = updatedState.tableIdToPlayerIdsMap[tableId]!!
        .filter { !playerToChipsAtTableMap.keys.contains(it) }.toSet()
    val inGameButMissingFromTableBustedEvents = inGameButMissingFromTablePlayers.map { bustPlayer(updatedState, it) }
    events.addAll(inGameButMissingFromTableBustedEvents)
    updatedState = applyEvents(updatedState, inGameButMissingFromTableBustedEvents)

    return Pair(updatedState, events)
}

private fun bustPlayer(state: GameState, playerId: UUID): PlayerBustedGameEvent {
    require(state.tableIdToPlayerIdsMap.flatMap { it.value }.any { it == playerId }) { "player is not active in the game" }
    return PlayerBustedGameEvent(state.aggregateId, playerId)
}
