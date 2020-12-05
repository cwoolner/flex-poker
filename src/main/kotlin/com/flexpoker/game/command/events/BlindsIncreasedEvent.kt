package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

data class BlindsIncreasedEvent @JsonCreator constructor(@JsonProperty(value = "gameId") val gameId: UUID) :
    BaseGameEvent(gameId), GameEvent