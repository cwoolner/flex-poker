package com.flexpoker.core.chat;

import java.security.Principal;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendChatMessageCommand;
import com.flexpoker.core.api.chat.SendGlobalChatMessageCommand;
import com.flexpoker.model.chat.ChatMessage;
import com.flexpoker.model.chat.GlobalChatMessage;

@Command
public class SendChatMessageRouterCommand implements SendChatMessageCommand {

    private final SendGlobalChatMessageCommand sendGlobalChatMessageCommand;

    @Inject
    public SendChatMessageRouterCommand(SendGlobalChatMessageCommand sendGlobalChatMessageCommand) {
        this.sendGlobalChatMessageCommand = sendGlobalChatMessageCommand;
    }

    @Override
    public void execute(ChatMessage chatMessage, Principal principal) {
        sendGlobalChatMessageCommand.execute(new GlobalChatMessage(chatMessage.getMessage(), principal.getName(), false));
    }

}
