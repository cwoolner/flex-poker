package com.flexpoker.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder

val passwordEncoder = DelegatingPasswordEncoder("bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder()))

fun encodePassword(password: String): String {
    return passwordEncoder.encode(password)
}
