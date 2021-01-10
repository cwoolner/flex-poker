package com.flexpoker.table.command.aggregate.eventproducers

import com.flexpoker.table.command.aggregate.TableState

fun checkHandIsBeingPlayed(state: TableState) {
    requireNotNull(state.currentHand) { "no hand in progress" }
}

fun numberOfPlayersAtTable(state: TableState): Int {
    return state.seatMap.values.filterNotNull().count()
}