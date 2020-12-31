package com.flexpoker.table.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.PlayerAction
import java.util.UUID


fun checkActionOnPlayer(state: HandState, playerId: UUID) {
    val actionOnPlayerId = state.seatMap[state.actionOnPosition]
    if (playerId != actionOnPlayerId) {
        throw FlexPokerException("action is not on the player attempting action")
    }
}

fun checkPerformAction(state: HandState, playerId: UUID, playerAction: PlayerAction) {
    if (!state.possibleSeatActionsMap[playerId]!!.contains(playerAction)) {
        throw FlexPokerException("not allowed to $playerAction")
    }
}

fun checkHandIsBeingPlayed(state: TableState) {
    if (state.currentHand == null) {
        throw FlexPokerException("no hand in progress")
    }
}