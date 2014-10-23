package com.flexpoker.login.query.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Profile("dev")
@Repository
public class InMemoryLoginRepository implements LoginRepository {

    private final Map<String, UserDetails> loginUserMap;

    public InMemoryLoginRepository() {
        loginUserMap = new HashMap<>();
        addDefaultUsers();
    }

    private void addDefaultUsers() {
        saveLogin("player1", new BCryptPasswordEncoder().encode("player1"));
        saveLogin("player2", new BCryptPasswordEncoder().encode("player2"));
        saveLogin("player3", new BCryptPasswordEncoder().encode("player3"));
        saveLogin("player4", new BCryptPasswordEncoder().encode("player4"));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return loginUserMap.get(username);
    }

    @Override
    public void saveLogin(String username, String encryptedPassword) {
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

        loginUserMap.put(username, userDetails);
    }
}
