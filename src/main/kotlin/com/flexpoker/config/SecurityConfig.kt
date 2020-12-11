package com.flexpoker.config

import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.util.PasswordUtils
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import javax.inject.Inject

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Inject
    private val loginRepository: LoginRepository? = null

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(loginRepository).passwordEncoder(PasswordUtils.encoder)
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/sign-up**").anonymous()
            .anyRequest().hasRole("USER")
            .and().formLogin().loginPage("/login").defaultSuccessUrl("/", true).permitAll()
            .and().rememberMe()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/resources/**")
    }

}