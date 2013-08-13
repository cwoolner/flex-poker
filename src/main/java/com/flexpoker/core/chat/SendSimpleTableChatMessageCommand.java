package com.flexpoker.core.chat;

import javax.inject.Inject;

import org.springframework.messaging.core.MessageSendingOperations;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.model.chat.outgoing.TableChatMessage;
import com.flexpoker.util.MessagingConstants;

@Command
public class SendSimpleTableChatMessageCommand implements SendTableChatMessageCommand {

    private final MessageSendingOperations<String> messagingTemplate;

    @Inject
    public SendSimpleTableChatMessageCommand(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void execute(TableChatMessage chatMessage) {
        if (chatMessage.isSystemMessage()) {
            messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_TABLE_SYSTEM, chatMessage.getTableId()),
                    "System: " + chatMessage.getMessage());
        } else {
            messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_TABLE_USER, chatMessage.getTableId()),
                    chatMessage.getSenderUsername() + ": " + chatMessage.getMessage());
        }
    }

}
