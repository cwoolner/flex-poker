package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.HandDealerState
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.TableEvent

fun finishHandIfAppropriate(state: HandState): List<TableEvent> {
    return if (state.handDealerState === HandDealerState.COMPLETE)
        listOf(HandCompletedEvent(state.tableId, state.gameId, state.entityId, state.chipsInBackMap))
    else emptyList()
}