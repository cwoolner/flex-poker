package com.flexpoker.game.command.events

import java.util.UUID

data class PlayerBustedGameEvent (val gameId: UUID, val playerId: UUID) : BaseGameEvent(gameId)