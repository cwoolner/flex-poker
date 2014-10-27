package com.flexpoker.login.query.repository;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Profile("prod")
@Repository
public class RedisLoginRepository implements LoginRepository {

    private static final String LOGIN_NAMESPACE = "login:";

    private final StringRedisTemplate redisTemplate;

    @Inject
    public RedisLoginRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        addDefaultUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        String encryptedPassword = redisTemplate.opsForValue().get(
                LOGIN_NAMESPACE + username);

        if (encryptedPassword == null) {
            return null;
        }

        // TODO: change these to not use default positive values, getting all of
        // the data from Redis instead
        UserDetails userDetails = new UserDetails() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return encryptedPassword;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            }
        };

        return userDetails;
    }

    @Override
    public void saveLogin(String username, String encryptedPassword) {
        redisTemplate.opsForValue().set(LOGIN_NAMESPACE + username, encryptedPassword);
    }

    private void addDefaultUsers() {
        saveLogin("player1", new BCryptPasswordEncoder().encode("player1"));
        saveLogin("player2", new BCryptPasswordEncoder().encode("player2"));
        saveLogin("player3", new BCryptPasswordEncoder().encode("player3"));
        saveLogin("player4", new BCryptPasswordEncoder().encode("player4"));
    }

}
