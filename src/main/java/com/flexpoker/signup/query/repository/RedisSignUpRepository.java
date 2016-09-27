package com.flexpoker.signup.query.repository;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.signup.command.aggregate.SignUpUser;

@Profile("prod")
@Repository
public class RedisSignUpRepository implements SignUpRepository {

    private static final int EXPIRATION_IN_MINUTES = 10;

    private static final String SIGN_UP_NAMESPACE = "signup:";

    private static final String EXISTING_USERNAME_KEY = SIGN_UP_NAMESPACE
            + "existingusernames";

    private static final String SIGN_UP_CODE_NAMESPACE = SIGN_UP_NAMESPACE
            + "signupcode:";

    private static final String SIGN_UP_USER_NAMESPACE = SIGN_UP_NAMESPACE
            + "signupuser:";

    private final StringRedisTemplate redisTemplate;

    @Inject
    public RedisSignUpRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        addDefaultSignUps();
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

    @Override
    public UUID findSignUpCodeByUsername(String username) {
        String foundSignUpCodeKey = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try (Cursor<byte[]> cursor = connection.scan(
                        ScanOptions.scanOptions().match("signup:signupcode*").build())) {
                    while (cursor.hasNext()) {
                        String key = new String(cursor.next(), "UTF-8");
                        String usernameFromRedis = (String) redisTemplate.opsForHash().get(key, "username");
                        if (username.equals(usernameFromRedis)) {
                            return key;
                        }
                    }
                } catch (IOException e) {
                    throw new FlexPokerException("error in Redis");
                }
                throw new FlexPokerException("could not find username in Redis");
            }
        });

        return UUID.fromString(foundSignUpCodeKey.split(":")[2]);
    }

    private void addDefaultSignUps() {
        storeNewlyConfirmedUsername("player1");
        storeNewlyConfirmedUsername("player2");
        storeNewlyConfirmedUsername("player3");
        storeNewlyConfirmedUsername("player4");
    }

    @Override
    public SignUpUser fetchSignUpUser(UUID signUpUserId) {
        String signUpCodeKey = SIGN_UP_USER_NAMESPACE + signUpUserId;
        Object objectFromQuery = redisTemplate.opsForHash()
                .get(signUpCodeKey, "username");

        if (objectFromQuery == null) {
            return null;
        }

        String usernameFromQuery = (String) objectFromQuery;

//        if (usernameFromQuery.equals(username)) {
//            return UUID.fromString((String) redisTemplate.opsForHash().get(signUpCodeKey,
//                    "aggregateid"));
//        }

        return null;
    }

    @Override
    public void saveSignUpUser(SignUpUser signUpUser) {
        // TODO Auto-generated method stub
        
    }

}
