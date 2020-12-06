package com.flexpoker.game.command.events

import java.util.UUID

data class TablePausedForBalancingEvent (val gameId: UUID, val tableId: UUID) : BaseGameEvent(gameId)