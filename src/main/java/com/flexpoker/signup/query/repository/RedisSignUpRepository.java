package com.flexpoker.signup.query.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Profile("prod")
@Repository
public class RedisSignUpRepository implements SignUpRepository {

    private static final int EXPIRATION_IN_MINUTES = 10;

    private static final String SIGN_UP_NAMESPACE = "signup:";

    private static final String EXISTING_USERNAME_KEY = "existingusernames";

    private static final String SIGN_UP_CODE_NAMESPACE = SIGN_UP_NAMESPACE
            + "signupcode:";

    private final StringRedisTemplate redisTemplate;

    @Inject
    public RedisSignUpRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean usernameExists(String username) {
        return redisTemplate.opsForSet().isMember(EXISTING_USERNAME_KEY, username);
    }

    @Override
    public boolean signUpCodeExists(UUID signUpCode) {
        String signUpCodeKey = SIGN_UP_CODE_NAMESPACE + signUpCode.toString();
        return redisTemplate.opsForHash().hasKey(signUpCodeKey, "username");
    }

    @Override
    public UUID findAggregateIdByUsernameAndSignUpCode(String username, UUID signUpCode) {
        String signUpCodeKey = SIGN_UP_CODE_NAMESPACE + signUpCode.toString();
        Object objectFromQuery = redisTemplate.opsForHash()
                .get(signUpCodeKey, "username");

        if (objectFromQuery == null) {
            return null;
        }

        String usernameFromQuery = (String) objectFromQuery;

        if (usernameFromQuery.equals(username)) {
            return UUID.fromString((String) redisTemplate.opsForHash().get(signUpCodeKey,
                    "aggregateid"));
        }

        return null;
    }

    @Override
    public void storeSignUpInformation(UUID aggregateId, String username, UUID signUpCode) {
        String signUpCodeKey = SIGN_UP_CODE_NAMESPACE + signUpCode.toString();

        redisTemplate.multi();
        redisTemplate.opsForHash().put(signUpCodeKey, "username", username);
        redisTemplate.opsForHash().put(signUpCodeKey, "aggregateid",
                aggregateId.toString());
        redisTemplate.expire(signUpCodeKey, EXPIRATION_IN_MINUTES, TimeUnit.MINUTES);
        redisTemplate.exec();
    }

    @Override
    public void storeNewlyConfirmedUsername(String username) {
        redisTemplate.opsForSet().add(EXISTING_USERNAME_KEY, username);
    }

}
