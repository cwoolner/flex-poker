package com.flexpoker.web.dto

import java.util.UUID

data class CallTableActionDTO (val gameId: UUID, val tableId: UUID)

data class IncomingChatMessageDTO (val message: String, val receiverUsernames: List<String>?,
                                   val gameId: UUID?, val tableId: UUID?)

data class CheckTableActionDTO (val gameId: UUID, val tableId: UUID)

data class CreateGameDTO (val name: String, val players: Int, val playersPerTable: Int,
                          val numberOfMinutesBetweenBlindLevels: Int, val numberOfSecondsForActionOnTimer: Int)

data class FoldTableActionDTO (val gameId: UUID, val tableId: UUID)

data class RaiseTableActionDTO (val gameId: UUID, val tableId: UUID, val raiseToAmount: Int)