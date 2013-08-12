package com.flexpoker.core.chat;

import javax.inject.Inject;

import org.springframework.messaging.core.MessageSendingOperations;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGlobalChatMessageCommand;
import com.flexpoker.model.chat.GlobalChatMessage;
import com.flexpoker.util.MessagingConstants;

@Command
public class SendSimpleGlobalChatMessageCommand implements SendGlobalChatMessageCommand {

    private final MessageSendingOperations<String> messagingTemplate;

    @Inject
    public SendSimpleGlobalChatMessageCommand(MessageSendingOperations<String> messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void execute(GlobalChatMessage chatMessage) {
        if (chatMessage.isSystemMessage()) {
            messagingTemplate.convertAndSend(MessagingConstants.CHAT_GLOBAL_SYSTEM,
                    "System: " + chatMessage.getMessage());
        } else {
            messagingTemplate.convertAndSend(MessagingConstants.CHAT_GLOBAL_USER,
                    chatMessage.getUsername() + ": " + chatMessage.getMessage());
        }
    }

}
