package com.flexpoker.game.command.events

import java.util.UUID

data class GameCreatedEvent (
    val gameId: UUID,
    val gameName: String,
    val numberOfPlayers: Int,
    val numberOfPlayersPerTable: Int,
    val createdByPlayerId: UUID,
    val numberOfMinutesBetweenBlindLevels: Int,
    val numberOfSecondsForActionOnTimer: Int) : BaseGameEvent(gameId)