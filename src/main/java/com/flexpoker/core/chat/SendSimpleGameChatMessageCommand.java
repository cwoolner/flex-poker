package com.flexpoker.core.chat;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.util.MessagingConstants;

@Component
public class SendSimpleGameChatMessageCommand implements SendGameChatMessageCommand {

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public SendSimpleGameChatMessageCommand(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void execute(GameChatMessage chatMessage) {
        if (chatMessage.isSystemMessage()) {
            messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_GAME_SYSTEM, chatMessage.getGameId()),
                    "System: " + chatMessage.getMessage());
        } else {
            messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_GAME_USER, chatMessage.getGameId()),
                    chatMessage.getSenderUsername() + ": " + chatMessage.getMessage());
        }
    }

}
