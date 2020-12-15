package com.flexpoker.game.command.factory

import com.flexpoker.game.command.aggregate.Game
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameEvent

interface GameFactory {
    fun createNew(command: CreateGameCommand): Game
    fun createFrom(events: List<GameEvent>): Game
}