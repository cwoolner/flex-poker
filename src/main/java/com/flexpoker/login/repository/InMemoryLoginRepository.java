package com.flexpoker.login.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Repository;

@Profile("default")
@Repository
public class InMemoryLoginRepository implements LoginRepository {

    private final Map<String, UserDetails> loginUserMap;

    private final Map<String, UUID> loginIdMap;

    private final Map<UUID, String> aggregateIdUsernameMap;

    public InMemoryLoginRepository() {
        loginUserMap = new HashMap<>();
        loginIdMap = new HashMap<>();
        aggregateIdUsernameMap = new HashMap<>();
        addDefaultUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var persistedUserDetails = loginUserMap.get(username);
        return new User(
                persistedUserDetails.getUsername(),
                persistedUserDetails.getPassword(),
                persistedUserDetails.getAuthorities());
    }

    @Override
    public void saveUsernameAndPassword(String username, String encryptedPassword) {
        loginUserMap.put(username, new User(username, encryptedPassword,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
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
        var passwordEncoder = new DelegatingPasswordEncoder("bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));

        saveUsernameAndPassword("player1", passwordEncoder.encode("player1"));
        saveUsernameAndPassword("player2", passwordEncoder.encode("player2"));
        saveUsernameAndPassword("player3", passwordEncoder.encode("player3"));
        saveUsernameAndPassword("player4", passwordEncoder.encode("player4"));

        saveAggregateIdAndUsername(UUID.randomUUID(), "player1");
        saveAggregateIdAndUsername(UUID.randomUUID(), "player2");
        saveAggregateIdAndUsername(UUID.randomUUID(), "player3");
        saveAggregateIdAndUsername(UUID.randomUUID(), "player4");
    }

}
