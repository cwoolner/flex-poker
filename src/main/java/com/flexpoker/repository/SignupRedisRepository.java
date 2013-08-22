package com.flexpoker.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.repository.api.SignupRepository;

@Repository
public class SignupRedisRepository implements SignupRepository {

    private final StringRedisTemplate redisTemplate;
    
    @Inject
    public SignupRedisRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public void storeSignupCode(String username, UUID signupCode, int expirationInMinutes) {
        redisTemplate.opsForValue().set(username, signupCode.toString());
        redisTemplate.expire(username, expirationInMinutes, TimeUnit.MINUTES);
    }

    @Override
    public UUID fetchSignupCode(String username) {
        String signupCode = redisTemplate.opsForValue().get(username);
        if (signupCode == null) {
            return null;
        }
        return UUID.fromString(signupCode);
    }

    @Override
    public void removeSignupCode(String username) {
        redisTemplate.delete(username);
    }

}
