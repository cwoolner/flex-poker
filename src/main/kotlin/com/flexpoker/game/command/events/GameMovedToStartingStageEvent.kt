package com.flexpoker.game.command.events

import java.util.UUID

data class GameMovedToStartingStageEvent (val gameId: UUID) : BaseGameEvent(gameId)