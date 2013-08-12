package com.flexpoker.bso;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.ChatBso;
import com.flexpoker.core.api.chat.SendChatMessageCommand;
import com.flexpoker.model.chat.ChatMessage;

@Service
public class ChatBsoImpl implements ChatBso {

    private final SendChatMessageCommand sendChatMessageCommand;

    @Inject
    public ChatBsoImpl(SendChatMessageCommand sendChatMessageCommand) {
        this.sendChatMessageCommand = sendChatMessageCommand;
    }

    @Override
    public void sendChatMessage(ChatMessage chatMessage, Principal principal) {
        sendChatMessageCommand.execute(chatMessage, principal);
    }

}
