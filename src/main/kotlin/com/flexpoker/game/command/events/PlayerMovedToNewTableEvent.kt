package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

class PlayerMovedToNewTableEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @JsonProperty(value = "fromTableId") val fromTableId: UUID,
    @JsonProperty(value = "toTableId") val toTableId: UUID,
    @JsonProperty(value = "playerId") val playerId: UUID,
    @JsonProperty(value = "chips") val chips: Int
) : BaseGameEvent(gameId), GameEvent