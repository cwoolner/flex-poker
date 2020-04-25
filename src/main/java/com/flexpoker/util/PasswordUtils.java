package com.flexpoker.util;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

public class PasswordUtils {

    public static final DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(
            "bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));

    public static String encode(String password) {
        return encoder.encode(password);
    }

}
