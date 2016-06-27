package com.flexpoker.core.chat;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendPersonalChatMessageCommand;
import com.flexpoker.model.chat.outgoing.PersonalChatMessage;
import com.flexpoker.util.MessagingConstants;

@Component
public class SendSimplePersonalChatMessageCommand implements SendPersonalChatMessageCommand {

    private final SimpMessageSendingOperations messagingTemplate;
    
    @Inject
    public SendSimplePersonalChatMessageCommand(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void execute(PersonalChatMessage chatMessage) {
        for (String username : chatMessage.getReceiverUsernames()) {
            if (chatMessage.isSystemMessage()) {
                messagingTemplate.convertAndSendToUser(username, MessagingConstants.CHAT_PERSONAL_SYSTEM,
                        "System: " + chatMessage.getMessage());
            } else {
                messagingTemplate.convertAndSendToUser(username, MessagingConstants.CHAT_PERSONAL_USER,
                        chatMessage.getSenderUsername() + ": " + chatMessage.getMessage());
            }
        }
    }

}
