package com.flexpoker.table.command.aggregate

fun checkHandIsBeingPlayed(state: TableState) {
    requireNotNull(state.currentHand) { "no hand in progress" }
}

fun numberOfPlayersAtTable(state: TableState): Int {
    return state.seatMap.values.filterNotNull().count()
}