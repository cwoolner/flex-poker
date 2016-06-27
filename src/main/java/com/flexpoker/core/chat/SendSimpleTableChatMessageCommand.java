package com.flexpoker.core.chat;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.util.MessagingConstants;

@Component
public class SendSimpleTableChatMessageCommand implements SendTableChatMessageCommand {

    private final SimpMessageSendingOperations messagingTemplate;

    @Inject
    public SendSimpleTableChatMessageCommand(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void execute(TableChatMessage chatMessage) {
        if (chatMessage.isSystemMessage()) {
            messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_TABLE_SYSTEM,
                    chatMessage.getGameId(), chatMessage.getTableId()),
                    "System: " + chatMessage.getMessage());
        } else {
            messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_TABLE_USER,
                    chatMessage.getGameId(), chatMessage.getTableId()),
                    chatMessage.getSenderUsername() + ": " + chatMessage.getMessage());
        }
    }

}
