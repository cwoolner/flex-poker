package com.flexpoker.game.command.commands

import java.time.Instant
import java.util.UUID

sealed class GameCommand (val id: UUID = UUID.randomUUID(), val time: Instant = Instant.now())

data class AttemptToStartNewHandCommand(val aggregateId: UUID, val tableId: UUID,
                                        val playerToChipsAtTableMap: Map<UUID, Int>) : GameCommand()

data class CreateGameCommand(val gameName: String, val numberOfPlayers: Int, val numberOfPlayersPerTable: Int,
                             val createdByPlayerId: UUID, val numberOfMinutesBetweenBlindLevels: Int,
                             val numberOfSecondsForActionOnTimer: Int) : GameCommand()

data class IncrementBlindsCommand(val aggregateId: UUID) : GameCommand()

data class JoinGameCommand(val aggregateId: UUID, val playerId: UUID) : GameCommand()
