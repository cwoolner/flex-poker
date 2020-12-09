package com.flexpoker.web.controller;

import com.flexpoker.chat.repository.ChatRepository;
import com.flexpoker.chat.service.ChatService;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.web.dto.IncomingChatMessageDTO;
import com.flexpoker.web.dto.OutgoingChatMessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {

    private final PushNotificationPublisher pushNotificationPublisher;

    private final ChatRepository chatRepository;

    private final ChatService chatService;

    @Inject
    public ChatController(
            PushNotificationPublisher pushNotificationPublisher,
            ChatRepository chatRepository,
            ChatService chatService) {
        this.pushNotificationPublisher = pushNotificationPublisher;
        this.chatRepository = chatRepository;
        this.chatService = chatService;
    }

    @MessageMapping("/app/sendchatmessage")
    public void sendChatMessage(IncomingChatMessageDTO chatMessage, Principal principal) {
        var gameId = chatMessage.getGameId();
        var tableId = chatMessage.getTableId();
        var message = chatMessage.getMessage();
        var username = principal.getName();

        if (gameId != null && tableId != null) {
            chatService.saveAndPushUserTableChatMessage(gameId, tableId, message, username);
        } else if (gameId != null) {
            chatService.saveAndPushUserGameChatMessage(gameId, message, username);
        } else {
            chatService.saveAndPushUserLobbyChatMessage(message, username);
        }
    }

    @SubscribeMapping("/topic/chat/lobby")
    public List<OutgoingChatMessageDTO> fetchAllLobbyChatMessages() {
        return chatRepository.fetchAllLobbyChatMessages();
    }

    @SubscribeMapping("/topic/chat/game/{gameId}")
    public List<OutgoingChatMessageDTO> fetchAllGameChatMessages(@DestinationVariable UUID gameId) {
        return chatRepository.fetchAllGameChatMessages(gameId);
    }

    @SubscribeMapping("/topic/chat/game/{gameId}/table/{tableId}")
    public List<OutgoingChatMessageDTO> fetchAllTableChatMessages(
            @DestinationVariable UUID gameId, @DestinationVariable UUID tableId) {
        return chatRepository.fetchAllTableChatMessages(tableId);
    }

}
