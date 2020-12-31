package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.events.PlayerAddedEvent
import com.flexpoker.table.command.events.PlayerRemovedEvent
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID
import kotlin.random.Random

fun addPlayer(state: TableState, playerId: UUID, chips: Int): List<TableEvent> {
    require (!state.seatMap.values.contains(playerId)) { "player already at this table" }
    val newPlayerPosition = findRandomOpenSeat(state)
    return listOf(PlayerAddedEvent(state.aggregateId, state.gameId, playerId, chips, newPlayerPosition))
}

fun removePlayer(state: TableState, playerId: UUID): List<TableEvent> {
    require (state.seatMap.values.contains(playerId)) { "player not at this table" }
    require(state.currentHand == null) { "can't remove a player while in a hand" }
    return listOf(PlayerRemovedEvent(state.aggregateId, state.gameId, playerId))
}

private fun findRandomOpenSeat(state: TableState): Int {
    while (true) {
        val potentialNewPlayerPosition = Random.nextInt(state.seatMap.size)
        if (state.seatMap[potentialNewPlayerPosition] == null) {
            return potentialNewPlayerPosition
        }
    }
}
