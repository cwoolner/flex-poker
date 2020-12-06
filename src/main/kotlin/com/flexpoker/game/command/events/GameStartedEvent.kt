package com.flexpoker.game.command.events

import com.flexpoker.game.command.events.dto.BlindScheduleDTO
import java.util.UUID

data class GameStartedEvent (
    val gameId: UUID, val tableIds: Set<UUID>, val blindScheduleDTO: BlindScheduleDTO) : BaseGameEvent(gameId)