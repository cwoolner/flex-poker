package com.flexpoker.core.chat;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.core.MessageSendingOperations;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendPersonalChatMessageCommand;
import com.flexpoker.model.chat.outgoing.PersonalChatMessage;
import com.flexpoker.repository.api.UserDataRepository;
import com.flexpoker.util.MessagingConstants;

@Command
public class SendSimplePersonalChatMessageCommand implements SendPersonalChatMessageCommand {

    private final MessageSendingOperations<String> messagingTemplate;
    
    private final UserDataRepository userDataRepository;

    @Inject
    public SendSimplePersonalChatMessageCommand(
            MessageSendingOperations<String> messagingTemplate,
            UserDataRepository userDataRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public void execute(PersonalChatMessage chatMessage) {
        for (String username : chatMessage.getReceiverUsernames()) {
            UUID userChatUUID = userDataRepository.getPersonalChatId(username);
            if (chatMessage.isSystemMessage()) {
                messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_PERSONAL_SYSTEM, userChatUUID),
                        "System: " + chatMessage.getMessage());
            } else {
                messagingTemplate.convertAndSend(String.format(MessagingConstants.CHAT_PERSONAL_USER, userChatUUID),
                        chatMessage.getSenderUsername() + ": " + chatMessage.getMessage());
            }
        }
    }

}
