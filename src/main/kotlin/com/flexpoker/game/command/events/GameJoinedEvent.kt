package com.flexpoker.game.command.events

import java.util.UUID

data class GameJoinedEvent (val gameId: UUID, val playerId: UUID) : BaseGameEvent(gameId)