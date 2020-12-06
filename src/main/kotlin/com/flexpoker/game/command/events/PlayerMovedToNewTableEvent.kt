package com.flexpoker.game.command.events

import java.util.UUID

data class PlayerMovedToNewTableEvent (
    val gameId: UUID, val fromTableId: UUID, val toTableId: UUID, val playerId: UUID, val chips: Int) : BaseGameEvent(gameId)