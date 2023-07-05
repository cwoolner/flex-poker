package com.flexpoker.web.controller

import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.nio.charset.Charset

@Controller
class HomeController {

    @GetMapping(path = ["/", "/game/*", "/game/*/table/*"], produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun index(): String {
        return ClassPathResource("index.html").getContentAsString(Charset.defaultCharset())
    }

}