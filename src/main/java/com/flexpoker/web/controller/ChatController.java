package com.flexpoker.web.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.flexpoker.chat.repository.ChatRepository;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.pushnotifications.ChatSentPushNotification;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Controller
public class ChatController {

    private final PushNotificationPublisher pushNotificationPublisher;

    private final ChatRepository chatRepository;

    @Inject
    public ChatController(
            PushNotificationPublisher pushNotificationPublisher,
            ChatRepository chatRepository) {
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.chatRepository = chatRepository;
    }

    @MessageMapping("/app/sendchatmessage")
    public void sendChatMessage(com.flexpoker.web.dto.incoming.ChatMessageDTO chatMessage, Principal principal) {
        chatRepository.saveChatMessage(new ChatMessageDTO(chatMessage.getGameId(),
                chatMessage.getTableId(), chatMessage.getMessage(), principal.getName(), false));
        pushNotificationPublisher.publish(new ChatSentPushNotification(chatMessage.getGameId(),
                chatMessage.getTableId(), chatMessage.getMessage(), principal.getName(), false));
    }

    @SubscribeMapping("/topic/chat/global")
    public List<ChatMessageDTO> fetchAllGlobalChatMessages() {
        return chatRepository.fetchAllGlobalChatMessages();
    }

    @SubscribeMapping("/topic/chat/game/{gameId}")
    public List<ChatMessageDTO> fetchAllGameChatMessages(@DestinationVariable UUID gameId) {
        return chatRepository.fetchAllGameChatMessages(gameId);
    }

    @SubscribeMapping("/topic/chat/game/{gameId}/table/{tableId}")
    public List<ChatMessageDTO> fetchAllTableChatMessages(
            @DestinationVariable UUID gameId, @DestinationVariable UUID tableId) {
        return chatRepository.fetchAllTableChatMessages(tableId);
    }

}
