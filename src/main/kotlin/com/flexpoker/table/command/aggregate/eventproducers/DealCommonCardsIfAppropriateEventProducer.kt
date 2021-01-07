package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent

fun dealCommonCardsIfAppropriate(state: HandState): List<TableEvent> {
    if (state.handDealerState === HandDealerState.FLOP_DEALT && !state.flopDealt) {
        return listOf(FlopCardsDealtEvent(state.tableId, state.gameId, state.entityId))
    }
    if (state.handDealerState === HandDealerState.TURN_DEALT && !state.turnDealt) {
        return listOf(TurnCardDealtEvent(state.tableId, state.gameId, state.entityId))
    }
    if (state.handDealerState === HandDealerState.RIVER_DEALT && !state.riverDealt) {
        return listOf(RiverCardDealtEvent(state.tableId, state.gameId, state.entityId))
    }
    return emptyList()
}