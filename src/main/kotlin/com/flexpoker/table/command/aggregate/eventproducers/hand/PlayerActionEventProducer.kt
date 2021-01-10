package com.flexpoker.table.command.aggregate.eventproducers.hand

import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PlayerForceCheckedEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun autoMoveHandForward(state: HandState): List<TableEvent> {
    return if (state.chipsInBackMap.values.all { it == 0 })
        listOf(AutoMoveHandForwardEvent(state.tableId, state.gameId, state.entityId))
    else
        emptyList()
}

fun expireActionOn(state: HandState, playerId: UUID): List<TableEvent> {
    return if (state.callAmountsMap[playerId]!! == 0) check(state, playerId, true)
    else fold(state, playerId, true)
}

fun fold(state: HandState, playerId: UUID, forced: Boolean): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.FOLD)
    return if (forced) listOf(PlayerForceFoldedEvent(state.tableId, state.gameId, state.entityId, playerId))
    else listOf(PlayerFoldedEvent(state.tableId, state.gameId, state.entityId, playerId))
}

fun check(state: HandState, playerId: UUID, forced: Boolean): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.CHECK)
    return if (forced) listOf(PlayerForceCheckedEvent(state.tableId, state.gameId, state.entityId, playerId))
    else listOf(PlayerCheckedEvent(state.tableId, state.gameId, state.entityId, playerId))
}

fun call(state: HandState, playerId: UUID): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.CALL)
    return listOf(PlayerCalledEvent(state.tableId, state.gameId, state.entityId, playerId))
}

fun raise(state: HandState, playerId: UUID, raiseToAmount: Int): List<TableEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.RAISE)
    checkRaiseAmountValue(state, playerId, raiseToAmount)
    return listOf(PlayerRaisedEvent(state.tableId, state.gameId, state.entityId, playerId, raiseToAmount))
}

private fun checkRaiseAmountValue(state: HandState, playerId: UUID, raiseToAmount: Int) {
    val playersTotalChips = state.chipsInBackMap[playerId]!! + state.chipsInFrontMap[playerId]!!
    require(!(raiseToAmount < state.raiseToAmountsMap[playerId]!! || raiseToAmount > playersTotalChips)) { "Raise amount must be between the minimum and maximum values." }
}
