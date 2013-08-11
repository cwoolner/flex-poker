package com.flexpoker.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.GameBso;

@Controller
public class ApplicationController {

    private final GameBso gameBso;

    @Inject
    public ApplicationController(GameBso gameBso) {
        this.gameBso = gameBso;
    }

}
