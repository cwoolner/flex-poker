package com.flexpoker.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder

object PasswordUtils {

    val encoder = DelegatingPasswordEncoder("bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder()))

    fun encode(password: String): String {
        return encoder.encode(password)
    }
}