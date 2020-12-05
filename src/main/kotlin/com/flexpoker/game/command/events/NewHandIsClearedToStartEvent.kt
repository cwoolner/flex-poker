package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.events.dto.BlindAmountsDTO
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

class NewHandIsClearedToStartEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @param:JsonProperty(value = "tableId") val tableId: UUID,
    @param:JsonProperty(value = "blinds") val blinds: BlindAmountsDTO
) : BaseGameEvent(gameId), GameEvent