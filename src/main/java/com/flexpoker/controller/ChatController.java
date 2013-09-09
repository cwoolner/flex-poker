package com.flexpoker.controller;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @MessageMapping(value = "/sendchatmessage")
    public void sendChatMessage(ChatMessage chatMessage, Principal principal) {
        chatBso.sendChatMessage(chatMessage, principal);
    }

}
