package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

class TablePausedForBalancingEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @param:JsonProperty(value = "tableId") val tableId: UUID
) : BaseGameEvent(gameId), GameEvent