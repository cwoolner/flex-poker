package com.flexpoker.game.command.events

import java.util.UUID

data class TableRemovedEvent (val gameId: UUID, val tableId: UUID) : BaseGameEvent(gameId)