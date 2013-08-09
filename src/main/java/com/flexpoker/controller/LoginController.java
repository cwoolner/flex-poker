package com.flexpoker.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flexpoker.bso.api.GameBso;

@Controller
public class LoginController {

    private final GameBso gameBso;
    
    @Inject
    public LoginController(GameBso gameBso) {
        this.gameBso = gameBso;
    }
    
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String index() {
        return "login";
    }
    
    @RequestMapping(value="/dosomething", method = RequestMethod.GET)
    public String doSomething() {
        gameBso.doSomething();
        return null;
    }
}
