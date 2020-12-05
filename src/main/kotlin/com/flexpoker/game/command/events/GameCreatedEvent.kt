package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.game.command.framework.GameEvent
import java.util.UUID

class GameCreatedEvent @JsonCreator constructor(
    @JsonProperty(value = "gameId") gameId: UUID,
    @param:JsonProperty(value = "gameName") val gameName: String,
    @param:JsonProperty(value = "numberOfPlayers") val numberOfPlayers: Int,
    @param:JsonProperty(value = "numberOfPlayersPerTable") val numberOfPlayersPerTable: Int,
    @param:JsonProperty(value = "createdByPlayerId") val createdByPlayerId: UUID,
    @param:JsonProperty(value = "numberOfMinutesBetweenBlindLevels") val numberOfMinutesBetweenBlindLevels: Int,
    @param:JsonProperty(value = "numberOfSecondsForActionOnTimer") val numberOfSecondsForActionOnTimer: Int
) : BaseGameEvent(gameId), GameEvent