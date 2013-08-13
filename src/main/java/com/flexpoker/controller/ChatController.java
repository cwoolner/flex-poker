package com.flexpoker.controller;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeEvent;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.ChatBso;
import com.flexpoker.model.chat.incoming.ChatMessage;

@Controller
public class ChatController {

    private final ChatBso chatBso;

    @Inject
    public ChatController(ChatBso chatBso) {
        this.chatBso = chatBso;
    }

    @MessageMapping(value = "/app/sendchatmessage")
    public void sendChatMessage(ChatMessage chatMessage, Principal principal) {
        chatBso.sendChatMessage(chatMessage, principal);
    }
    
    @SubscribeEvent(value = "/app/personalchatid")
    public UUID fetchPersonalChatId(Principal principal) {
        return chatBso.fetchPersonalChatId(principal);
    }

}
