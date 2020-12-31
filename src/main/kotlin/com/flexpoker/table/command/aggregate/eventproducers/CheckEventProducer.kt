package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.checkActionOnPlayer
import com.flexpoker.table.command.aggregate.checkHandIsBeingPlayed
import com.flexpoker.table.command.aggregate.checkPerformAction
import com.flexpoker.table.command.aggregate.handleEndOfRound
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PlayerForceCheckedEvent
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun check(state: TableState, playerId: UUID): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    val playerCheckedEvents = check(state.currentHand!!, playerId, false)
    val endOfRoundEvents = handleEndOfRound(applyEvents(state, *playerCheckedEvents.toTypedArray()))
    return playerCheckedEvents.plus(endOfRoundEvents)
}

fun check(state: HandState, playerId: UUID, forced: Boolean): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.CHECK)
    return if (forced) listOf(PlayerForceCheckedEvent(state.tableId, state.gameId, state.entityId, playerId))
    else listOf(PlayerCheckedEvent(state.tableId, state.gameId, state.entityId, playerId))
}