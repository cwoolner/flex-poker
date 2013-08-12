package com.flexpoker.bso.api;

import java.security.Principal;

import com.flexpoker.model.chat.ChatMessage;

public interface ChatBso {

    void sendChatMessage(ChatMessage chatMessage, Principal principal);

}
