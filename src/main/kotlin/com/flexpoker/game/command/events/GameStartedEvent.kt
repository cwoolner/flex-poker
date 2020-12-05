package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.events.dto.BlindScheduleDTO
import com.flexpoker.game.command.framework.GameEvent
import java.util.Collections
import java.util.UUID

class GameStartedEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @JsonProperty(value = "tableIds") tableIds: Set<UUID>?,
    @JsonProperty(value = "blindSchedule") blindScheduleDTO: BlindScheduleDTO
) : BaseGameEvent(gameId), GameEvent {
    val tableIds: Set<UUID>
    val blindScheduleDTO: BlindScheduleDTO

    init {
        this.tableIds = Collections.unmodifiableSet(tableIds)
        this.blindScheduleDTO = blindScheduleDTO
    }
}