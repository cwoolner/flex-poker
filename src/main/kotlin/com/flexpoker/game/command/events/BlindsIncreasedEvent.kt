package com.flexpoker.game.command.events

import java.util.UUID

data class BlindsIncreasedEvent (val gameId: UUID) : BaseGameEvent(gameId)