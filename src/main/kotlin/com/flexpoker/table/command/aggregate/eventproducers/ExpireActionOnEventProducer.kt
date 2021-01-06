package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.checkHandIsBeingPlayed
import com.flexpoker.table.command.aggregate.handleEndOfRound
import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

fun expireActionOn(state: TableState, handId: UUID?, playerId: UUID): List<TableEvent> {
    checkHandIsBeingPlayed(state)
    if (state.currentHand!!.entityId != handId) {
        emptyList<TableEvent>()
    }
    val forcedActionOnExpiredEvents = expireActionOn(state.currentHand, playerId)
    val endOfRoundEvents = handleEndOfRound(applyEvents(state, forcedActionOnExpiredEvents))
    return forcedActionOnExpiredEvents.plus(endOfRoundEvents)
}

fun expireActionOn(state: HandState, playerId: UUID): List<TableEvent> {
    return if (state.callAmountsMap[playerId]!! == 0) check(state, playerId, true)
    else fold(state, playerId, true)
}