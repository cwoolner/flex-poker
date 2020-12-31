package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.checkActionOnPlayer
import com.flexpoker.table.command.aggregate.checkPerformAction
import com.flexpoker.table.command.events.PlayerRaisedEvent
import java.util.UUID

fun raise(state: HandState, playerId: UUID, raiseToAmount: Int): List<PlayerRaisedEvent> {
    checkActionOnPlayer(state, playerId)
    checkPerformAction(state, playerId, PlayerAction.RAISE)
    checkRaiseAmountValue(state, playerId, raiseToAmount)
    return listOf(PlayerRaisedEvent(state.tableId, state.gameId, state.entityId, playerId, raiseToAmount))
}

fun checkRaiseAmountValue(state: HandState, playerId: UUID, raiseToAmount: Int) {
    val playersTotalChips = state.chipsInBackMap[playerId]!! + state.chipsInFrontMap[playerId]!!
    require(!(raiseToAmount < state.raiseToAmountsMap[playerId]!! || raiseToAmount > playersTotalChips)) { "Raise amount must be between the minimum and maximum values." }
}
