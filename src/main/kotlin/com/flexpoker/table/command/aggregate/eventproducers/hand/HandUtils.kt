package com.flexpoker.table.command.aggregate.eventproducers.hand

import com.flexpoker.table.command.PlayerAction
import com.flexpoker.table.command.aggregate.HandState
import java.util.UUID


fun checkActionOnPlayer(state: HandState, playerId: UUID) {
    require(playerId == state.seatMap[state.actionOnPosition]) { "action is not on the player attempting action" }
}

fun checkPerformAction(state: HandState, playerId: UUID, playerAction: PlayerAction) {
    require(state.possibleSeatActionsMap[playerId]!!.contains(playerAction)) { "not allowed to $playerAction" }
}