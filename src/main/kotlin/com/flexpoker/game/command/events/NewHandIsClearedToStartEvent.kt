package com.flexpoker.game.command.events

import com.flexpoker.game.command.events.dto.BlindAmountsDTO
import java.util.UUID

data class NewHandIsClearedToStartEvent (
    val gameId: UUID, val tableId: UUID, val blindAmountsDTO: BlindAmountsDTO) : BaseGameEvent(gameId)