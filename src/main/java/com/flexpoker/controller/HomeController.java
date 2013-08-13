package com.flexpoker.controller;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flexpoker.bso.api.ChatBso;

@Controller
public class HomeController {

    private final ChatBso chatBso;

    @Inject
    public HomeController(ChatBso chatBso) {
        this.chatBso = chatBso;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Principal principal) {
        // TODO: move this to a login post event handler
        chatBso.createPersonalChatId(principal);
        return "index";
    }
}
