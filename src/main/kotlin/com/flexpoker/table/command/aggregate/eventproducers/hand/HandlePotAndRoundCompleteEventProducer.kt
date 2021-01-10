package com.flexpoker.table.command.aggregate.eventproducers.hand

import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.aggregate.calculatePots
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableEvent

fun handlePotAndRoundCompleted(state: HandState): List<TableEvent> {
    var updatedState = state
    if (updatedState.seatMap[updatedState.actionOnPosition] != updatedState.lastToActPlayerId
        && updatedState.playersStillInHand.size > 1) {
        return emptyList()
    }

    val (potEvents, updatedPots) = calculatePots(updatedState.gameId, updatedState.tableId, updatedState.entityId,
        updatedState.pots, updatedState.handEvaluationList, updatedState.chipsInFrontMap, updatedState.chipsInBackMap)

    val tableEvents = ArrayList<TableEvent>()
    tableEvents.addAll(potEvents)
    updatedState = updatedState.copy(pots = updatedPots)
    val nextHandDealerState =
        if (updatedState.playersStillInHand.size == 1) HandDealerState.COMPLETE
        else HandDealerState.values()[updatedState.handDealerState.ordinal + 1]
    val roundCompletedEvent = RoundCompletedEvent(updatedState.tableId, updatedState.gameId,
        updatedState.entityId, nextHandDealerState)
    tableEvents.add(roundCompletedEvent)
    return tableEvents
}
