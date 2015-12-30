package com.flexpoker.login.query.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Profile("default")
@Repository
public class InMemoryLoginRepository implements LoginRepository {

    private static final Map<String, UserDetails> loginUserMap;

    private static final Map<String, UUID> loginIdMap;

    private static final Map<UUID, String> aggregateIdUsernameMap;

    /**
     * NOTE: Because this class is used by Spring Security, it is instantiated
     * in two different contexts. This means that the previous Map instance
     * variables were created twice. When the app stored a new player, it stored
     * it into a different Map than the one read by Spring Security, so InMemory
     * login was broken. Changing these to static fixes that issue. Not a huge
     * deal since a separate persistence store will be used when deploying the
     * system for real.
     */
    static {
        loginUserMap = new HashMap<>();
        loginIdMap = new HashMap<>();
        aggregateIdUsernameMap = new HashMap<>();
    }

    public InMemoryLoginRepository() {
        addDefaultUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return loginUserMap.get(username);
    }

    @Override
    public void saveUsernameAndPassword(String username, String encryptedPassword) {
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

    @Override
    public UUID fetchAggregateIdByUsername(String username) {
        return loginIdMap.get(username);
    }

    @Override
    public void saveAggregateIdAndUsername(UUID aggregateId, String username) {
        loginIdMap.put(username, aggregateId);
        aggregateIdUsernameMap.put(aggregateId, username);
    }

    @Override
    public String fetchUsernameByAggregateId(UUID aggregateId) {
        return aggregateIdUsernameMap.get(aggregateId);
    }

    private void addDefaultUsers() {
        saveUsernameAndPassword("player1", new BCryptPasswordEncoder().encode("player1"));
        saveUsernameAndPassword("player2", new BCryptPasswordEncoder().encode("player2"));
        saveUsernameAndPassword("player3", new BCryptPasswordEncoder().encode("player3"));
        saveUsernameAndPassword("player4", new BCryptPasswordEncoder().encode("player4"));

        saveAggregateIdAndUsername(UUID.randomUUID(), "player1");
        saveAggregateIdAndUsername(UUID.randomUUID(), "player2");
        saveAggregateIdAndUsername(UUID.randomUUID(), "player3");
        saveAggregateIdAndUsername(UUID.randomUUID(), "player4");
    }

}
