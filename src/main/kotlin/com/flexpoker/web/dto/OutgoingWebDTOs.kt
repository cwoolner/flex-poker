package com.flexpoker.web.dto

import java.util.UUID

data class OutgoingChatMessageDTO (val id: UUID, val gameId: UUID, val tableId: UUID, val message: String,
                                   val senderUsername: String, val systemMessage: Boolean)
