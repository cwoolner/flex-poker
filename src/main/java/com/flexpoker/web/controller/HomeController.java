package com.flexpoker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(path = {"/", "/game/*", "/game/*/table/*"})
    public String index() {
        return "index";
    }

}
