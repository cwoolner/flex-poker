package com.flexpoker.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping(path = ["/", "/game/*", "/game/*/table/*"])
    fun index(): String {
        return "index"
    }

}