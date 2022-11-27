package com.flexpoker.game.command.aggregate.eventproducers

import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.isMaxLevel
import com.flexpoker.game.command.events.BlindsIncreasedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.GameStage

fun increaseBlinds(state: GameState): List<GameEvent> {
    require(state.stage === GameStage.INPROGRESS) { "cannot increase blinds if the game isn't in progress" }
    return if (!isMaxLevel(state)) listOf(BlindsIncreasedEvent(state.aggregateId)) else emptyList()
}