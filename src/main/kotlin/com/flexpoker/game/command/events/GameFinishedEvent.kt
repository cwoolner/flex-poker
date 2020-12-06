package com.flexpoker.game.command.events

import java.util.UUID

data class GameFinishedEvent (val gameId: UUID) : BaseGameEvent(gameId)