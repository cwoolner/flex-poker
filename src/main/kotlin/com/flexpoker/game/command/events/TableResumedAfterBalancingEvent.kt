package com.flexpoker.game.command.events

import java.util.UUID

data class TableResumedAfterBalancingEvent (val gameId: UUID, val tableId: UUID) : BaseGameEvent(gameId)