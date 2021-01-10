package com.flexpoker.table.command.aggregate.eventproducers.hand

import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.aggregate.HandState
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent

fun dealCommonCardsIfAppropriate(state: HandState): List<TableEvent> {
    return when {
        state.handDealerState === HandDealerState.FLOP_DEALT && !state.flopDealt ->
            listOf(FlopCardsDealtEvent(state.tableId, state.gameId, state.entityId))
        state.handDealerState === HandDealerState.TURN_DEALT && !state.turnDealt ->
            listOf(TurnCardDealtEvent(state.tableId, state.gameId, state.entityId))
        state.handDealerState === HandDealerState.RIVER_DEALT && !state.riverDealt ->
            listOf(RiverCardDealtEvent(state.tableId, state.gameId, state.entityId))
        else ->
            emptyList()
    }
}