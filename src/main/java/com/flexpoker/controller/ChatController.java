package com.flexpoker.controller;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.ChatBso;
import com.flexpoker.model.chat.ChatMessage;

@Controller
public class ChatController {

    private final ChatBso chatBso;
    
    @Inject
    public ChatController(ChatBso chatBso) {
        this.chatBso = chatBso;
    }
    
    @MessageMapping(value="/app/sendchatmessage")
    public void createGame(ChatMessage chatMessage, Principal principal) {
        chatBso.sendChatMessage(chatMessage, principal);
    }

}
