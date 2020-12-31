package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.TableEvent

fun autoMoveHandForward(state: HandState): List<TableEvent> {
    return if (state.chipsInBackMap.values.all { it == 0 })
        listOf(AutoMoveHandForwardEvent(state.tableId, state.gameId, state.entityId))
    else
        emptyList()
}