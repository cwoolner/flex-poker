package com.flexpoker.web.controller

import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.nio.charset.Charset
import java.security.Principal

@Controller
class LoginController {

    @GetMapping("/login", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun index(): String = ClassPathResource("index.html").getContentAsString(Charset.defaultCharset())

    @GetMapping("/userinfo")
    fun userInfo(principal: Principal?): ResponseEntity<Map<String, Any?>> {
        val username = principal?.name
        return ResponseEntity(mapOf("username" to username, "loggedIn" to (username != null)), HttpStatus.OK)
    }

}