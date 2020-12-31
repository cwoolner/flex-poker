package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TablePausedEvent
import com.flexpoker.table.command.events.TableResumedEvent

fun pause(state: TableState): List<TableEvent> {
    if (state.paused) {
        throw FlexPokerException("table is already paused.  can't pause again.")
    }
    return listOf(TablePausedEvent(state.aggregateId, state.gameId))
}

fun resume(state: TableState): List<TableEvent> {
    if (!state.paused) {
        throw FlexPokerException("table is not paused.  can't resume.")
    }
    return listOf(TableResumedEvent(state.aggregateId, state.gameId))
}
