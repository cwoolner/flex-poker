package com.flexpoker.chat.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.web.dto.OutgoingChatMessageDTO
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Profile(ProfileNames.DEFAULT, ProfileNames.CHAT_INMEMORY)
@Repository
open class InMemoryChatRepository : ChatRepository {

    private val lobbyChatMessages: MutableList<OutgoingChatMessageDTO> = CopyOnWriteArrayList()
    private val gameChatMessages: MutableMap<UUID, MutableList<OutgoingChatMessageDTO>> = ConcurrentHashMap()
    private val tableChatMessages: MutableMap<UUID, MutableList<OutgoingChatMessageDTO>> = ConcurrentHashMap()

    override fun saveChatMessage(chatMessage: OutgoingChatMessageDTO) {
        if (chatMessage.gameId != null && chatMessage.tableId != null) {
            tableChatMessages.putIfAbsent(chatMessage.tableId, CopyOnWriteArrayList())
            tableChatMessages[chatMessage.tableId]!!.add(chatMessage)
        } else if (chatMessage.gameId != null) {
            gameChatMessages.putIfAbsent(chatMessage.gameId, CopyOnWriteArrayList())
            gameChatMessages[chatMessage.gameId]!!.add(chatMessage)
        } else {
            lobbyChatMessages.add(chatMessage)
        }
    }

    override fun fetchAllLobbyChatMessages(): List<OutgoingChatMessageDTO> {
        return lobbyChatMessages
    }

    override fun fetchAllGameChatMessages(gameId: UUID): List<OutgoingChatMessageDTO> {
        return gameChatMessages[gameId] ?: CopyOnWriteArrayList()
    }

    override fun fetchAllTableChatMessages(tableId: UUID): List<OutgoingChatMessageDTO> {
        return tableChatMessages[tableId] ?: CopyOnWriteArrayList()
    }

}