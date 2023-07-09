package com.flexpoker.config

import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.util.PasswordUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import javax.inject.Inject

@Configuration
@EnableWebSecurity
class SecurityConfig @Inject constructor(private val loginRepository: LoginRepository) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordUtils.encoder
    }

    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers(
                "/login",
                "/sign-up",
                "/sign-up-confirm",
                "/userinfo",
            ).permitAll()
            it.requestMatchers("/resources/**").permitAll()
            it.anyRequest().authenticated()
        }.formLogin {
            it.loginPage("/login").permitAll()
        }.csrf { it.disable() }
        return http.build()
    }

}
