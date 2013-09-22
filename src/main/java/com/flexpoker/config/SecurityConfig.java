package com.flexpoker.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("player1").password("player1").roles("USER").and()
                .withUser("player2").password("player2").roles("USER").and()
                .withUser("player3").password("player3").roles("USER").and()
                .withUser("player4").password("player4").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeUrls()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/signup**").anonymous()
            .anyRequest().hasRole("USER")
            .and()
        .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
            .and()
        .rememberMe();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

}
