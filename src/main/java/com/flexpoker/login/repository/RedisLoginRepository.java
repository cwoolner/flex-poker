package com.flexpoker.login.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Profile("login-redis")
@Repository
public class RedisLoginRepository implements LoginRepository {

    private static final String LOGIN_PASSWORD_NAMESPACE = "login-password:";

    private static final String LOGIN_ID_NAMESPACE = "login-id:";

    private static final String AGGREGATE_ID_USERNAME_NAMESPACE = "aggregateid-username:";

    private final StringRedisTemplate redisTemplate;

    @Inject
    public RedisLoginRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        addDefaultUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var encryptedPassword = redisTemplate.opsForValue().get(LOGIN_PASSWORD_NAMESPACE + username);

        if (encryptedPassword == null) {
            return null;
        }

        // TODO: change these to not use default positive values, getting all of
        // the data from Redis instead
        var userDetails = new UserDetails() {

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
    public void saveUsernameAndPassword(String username, String encryptedPassword) {
        redisTemplate.opsForValue().set(LOGIN_PASSWORD_NAMESPACE + username,
                encryptedPassword);
    }

    @Override
    public UUID fetchAggregateIdByUsername(String username) {
        String stringAggregateId = redisTemplate.opsForValue().get(
                LOGIN_ID_NAMESPACE + username);
        return UUID.fromString(stringAggregateId);
    }

    @Override
    public void saveAggregateIdAndUsername(UUID aggregateId, String username) {
        redisTemplate.opsForValue().set(LOGIN_ID_NAMESPACE + username,
                aggregateId.toString());
        redisTemplate.opsForValue().set(AGGREGATE_ID_USERNAME_NAMESPACE + aggregateId,
                username);
    }

    @Override
    public String fetchUsernameByAggregateId(UUID aggregateId) {
        return redisTemplate.opsForValue().get(
                AGGREGATE_ID_USERNAME_NAMESPACE + aggregateId);
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
