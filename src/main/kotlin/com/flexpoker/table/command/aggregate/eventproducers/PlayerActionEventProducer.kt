package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.hand.call
import com.flexpoker.table.command.aggregate.eventproducers.hand.changeActionOn
import com.flexpoker.table.command.aggregate.eventproducers.hand.check
import com.flexpoker.table.command.aggregate.eventproducers.hand.expireActionOn
import com.flexpoker.table.command.aggregate.eventproducers.hand.fold
import com.flexpoker.table.command.aggregate.eventproducers.hand.raise
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun autoMoveHandForward(state: TableState): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    return handleEndOfRound(state)
}

fun expireActionOn(state: TableState, handId: UUID?, playerId: UUID): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    if (state.currentHand!!.entityId != handId) {
        emptyList<TableEvent>()
    }
    val forcedActionOnExpiredEvents = expireActionOn(state.currentHand, playerId)
    val endOfRoundEvents = handleEndOfRound(applyEvents(state, forcedActionOnExpiredEvents))
    return forcedActionOnExpiredEvents.plus(endOfRoundEvents)
}

fun fold(state: TableState, playerId: UUID): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    val playerFoldedEvents = fold(state.currentHand!!, playerId, false)
    val endOfRoundEvents = handleEndOfRound(applyEvents(state, playerFoldedEvents))
    return playerFoldedEvents + endOfRoundEvents
}

fun check(state: TableState, playerId: UUID): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    val playerCheckedEvents = check(state.currentHand!!, playerId, false)
    val endOfRoundEvents = handleEndOfRound(applyEvents(state, playerCheckedEvents))
    return playerCheckedEvents.plus(endOfRoundEvents)
}

fun call(state: TableState, playerId: UUID): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    val playerCalledEvents = call(state.currentHand!!, playerId)
    val endOfRoundEvents = handleEndOfRound(applyEvents(state, playerCalledEvents))
    return playerCalledEvents.plus(endOfRoundEvents)
}

fun raise(state: TableState, playerId: UUID, raiseToAmount: Int): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    val playerRaisedEvents = raise(state.currentHand!!, playerId, raiseToAmount)
    val changeActionOnEvents = changeActionOn(applyEvents(state, playerRaisedEvents).currentHand!!)
    return playerRaisedEvents + changeActionOnEvents
}