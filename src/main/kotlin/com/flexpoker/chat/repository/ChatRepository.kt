package com.flexpoker.chat.repository

import com.flexpoker.web.dto.OutgoingChatMessageDTO
import java.util.UUID

interface ChatRepository {
    fun saveChatMessage(chatMessage: OutgoingChatMessageDTO)
    fun fetchAllLobbyChatMessages(): List<OutgoingChatMessageDTO>
    fun fetchAllGameChatMessages(gameId: UUID): List<OutgoingChatMessageDTO>
    fun fetchAllTableChatMessages(tableId: UUID): List<OutgoingChatMessageDTO>
}