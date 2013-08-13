package com.flexpoker.core.chat;

import javax.inject.Inject;

import org.springframework.messaging.core.MessageSendingOperations;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.model.chat.outgoing.GameChatMessage;
import com.flexpoker.util.MessagingConstants;

@Command
public class SendSimpleGameChatMessageCommand implements SendGameChatMessageCommand {

    private final MessageSendingOperations<String> messagingTemplate;

    @Inject
    public SendSimpleGameChatMessageCommand(MessageSendingOperations<String> messagingTemplate) {
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
