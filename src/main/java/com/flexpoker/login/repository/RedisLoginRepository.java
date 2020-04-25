package com.flexpoker.login.repository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;

@Profile({ ProfileNames.REDIS, ProfileNames.LOGIN_REDIS })
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(redisTemplate.opsForValue().get(LOGIN_PASSWORD_NAMESPACE + username))
                .map(password -> new User(username, password,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))))
                .orElseThrow(() -> new UsernameNotFoundException(username));
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
        var passwordEncoder = new DelegatingPasswordEncoder("bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));
        addUserIfDoesNotExist("player1", passwordEncoder);
        addUserIfDoesNotExist("player2", passwordEncoder);
        addUserIfDoesNotExist("player3", passwordEncoder);
        addUserIfDoesNotExist("player4", passwordEncoder);
    }

    private void addUserIfDoesNotExist(String username, DelegatingPasswordEncoder passwordEncoder) {
        if (!redisTemplate.opsForValue().getOperations().hasKey(LOGIN_PASSWORD_NAMESPACE + username)) {
            saveUsernameAndPassword(username, passwordEncoder.encode(username));
            saveAggregateIdAndUsername(UUID.randomUUID(), username);
        }
    }

}
