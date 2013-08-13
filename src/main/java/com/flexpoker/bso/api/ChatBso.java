package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.UUID;

import com.flexpoker.model.chat.incoming.ChatMessage;

public interface ChatBso {

    void sendChatMessage(ChatMessage chatMessage, Principal principal);

    UUID fetchPersonalChatId(Principal principal);

    void createPersonalChatId(Principal principal);

}
