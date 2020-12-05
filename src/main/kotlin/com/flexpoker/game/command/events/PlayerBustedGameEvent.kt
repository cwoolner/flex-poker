package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

class PlayerBustedGameEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @param:JsonProperty(value = "playerId") val playerId: UUID
) : BaseGameEvent(gameId), GameEvent