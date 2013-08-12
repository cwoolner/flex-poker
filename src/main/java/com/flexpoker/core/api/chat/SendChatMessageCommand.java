package com.flexpoker.core.api.chat;

import java.security.Principal;

import com.flexpoker.model.chat.incoming.ChatMessage;

public interface SendChatMessageCommand {

    void execute(ChatMessage chatMessage, Principal principal);

}
