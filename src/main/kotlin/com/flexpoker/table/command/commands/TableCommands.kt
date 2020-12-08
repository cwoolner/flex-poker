package com.flexpoker.table.command.commands

import java.time.Instant
import java.util.UUID

sealed class TableCommand(val id: UUID = UUID.randomUUID(), val time: Instant = Instant.now())

data class AddPlayerCommand(val tableId: UUID, val gameId: UUID, val playerId: UUID, val chips: Int) : TableCommand()

data class AutoMoveHandForwardCommand(val tableId: UUID, val gameId: UUID) : TableCommand()

data class CallCommand(val tableId: UUID, val gameId: UUID, val playerId: UUID) : TableCommand()

data class CheckCommand(val tableId: UUID, val gameId: UUID, val playerId: UUID) : TableCommand()

data class CreateTableCommand(val tableId: UUID, val gameId: UUID, val playerIds: Set<UUID>, val numberOfPlayersPerTable: Int) : TableCommand()

data class ExpireActionOnTimerCommand(val tableId: UUID, val gameId: UUID, val handId: UUID, val playerId: UUID) : TableCommand()

data class FoldCommand(val tableId: UUID, val gameId: UUID, val playerId: UUID) : TableCommand()

data class PauseCommand(val tableId: UUID, val gameId: UUID) : TableCommand()

data class RaiseCommand(val tableId: UUID, val gameId: UUID, val playerId: UUID, val raiseToAmount: Int) : TableCommand()

data class RemovePlayerCommand(val tableId: UUID, val gameId: UUID, val playerId: UUID) : TableCommand()

data class ResumeCommand(val tableId: UUID, val gameId: UUID) : TableCommand()

data class StartNewHandForExistingTableCommand(val tableId: UUID, val gameId: UUID,
                                               val smallBlind: Int, val bigBlind: Int) : TableCommand()

data class StartNewHandForNewGameCommand(val tableId: UUID, val gameId: UUID,
                                         val smallBlind: Int, val bigBlind: Int) : TableCommand()

data class TickActionOnTimerCommand(val tableId: UUID, val gameId: UUID,
                                    val handId: UUID, val number: Int) : TableCommand()
