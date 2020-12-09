package com.flexpoker.chat.service

import com.flexpoker.chat.repository.ChatRepository
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.ChatSentPushNotification
import com.flexpoker.web.dto.OutgoingChatMessageDTO
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DefaultChatService(private val chatRepository: ChatRepository,
                         private val pushNotificationPublisher: PushNotificationPublisher) : ChatService {

    override fun saveAndPushSystemLobbyChatMessage(message: String) {
        saveAndPush(null, null, message, null, true)
    }

    override fun saveAndPushUserLobbyChatMessage(message: String, username: String?) {
        saveAndPush(null, null, message, username, false)
    }

    override fun saveAndPushSystemGameChatMessage(gameId: UUID?, message: String) {
        saveAndPush(gameId, null, message, null, true)
    }

    override fun saveAndPushUserGameChatMessage(gameId: UUID?, message: String, username: String?) {
        saveAndPush(gameId, null, message, username, false)
    }

    override fun saveAndPushSystemTableChatMessage(gameId: UUID?, tableId: UUID?, message: String) {
        saveAndPush(gameId, tableId, message, null, true)
    }

    override fun saveAndPushUserTableChatMessage(gameId: UUID?, tableId: UUID?, message: String, username: String?) {
        saveAndPush(gameId, tableId, message, username, false)
    }

    private fun saveAndPush(gameId: UUID?, tableId: UUID?, message: String, username: String?, isSystemMessage: Boolean) {
        chatRepository.saveChatMessage(
            OutgoingChatMessageDTO(UUID.randomUUID(), gameId, tableId, message, username, isSystemMessage)
        )
        pushNotificationPublisher.publish(
            ChatSentPushNotification(gameId, tableId, message, username, isSystemMessage)
        )
    }
}