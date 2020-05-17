package com.flexpoker.chat.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.flexpoker.chat.repository.ChatRepository;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Service
public class DefaultChatService implements ChatService {

    private final ChatRepository chatRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    public DefaultChatService(ChatRepository chatRepository, PushNotificationPublisher pushNotificationPublisher) {
        this.chatRepository = chatRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void saveAndPushSystemLobbyChatMessage(String message) {
        saveAndPush(null, null, message, null, true);
    }

    @Override
    public void saveAndPushUserLobbyChatMessage(String message, String username) {
        saveAndPush(null, null, message, username, false);
    }

    @Override
    public void saveAndPushSystemGameChatMessage(UUID gameId, String message) {
        saveAndPush(gameId, null, message, null, true);
    }

    @Override
    public void saveAndPushUserGameChatMessage(UUID gameId, String message, String username) {
        saveAndPush(gameId, null, message, username, false);
    }

    @Override
    public void saveAndPushSystemTableChatMessage(UUID gameId, UUID tableId, String message) {
        saveAndPush(gameId, tableId, message, null, true);
    }

    @Override
    public void saveAndPushUserTableChatMessage(UUID gameId, UUID tableId, String message, String username) {
        saveAndPush(gameId, tableId, message, username, false);
    }

    private void saveAndPush(UUID gameId, UUID tableId, String message, String username, boolean isSystemMessage) {
        chatRepository.saveChatMessage(
                new ChatMessageDTO(gameId, tableId, message, username, isSystemMessage));
        pushNotificationPublisher.publish(
                new ChatSentPushNotification(gameId, tableId, message, username, isSystemMessage));
    }

}
