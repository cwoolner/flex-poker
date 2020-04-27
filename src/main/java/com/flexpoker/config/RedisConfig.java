package com.flexpoker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.table.command.framework.TableEvent;

@Configuration
@Profile({ ProfileNames.REDIS, ProfileNames.LOGIN_REDIS, ProfileNames.SIGNUP_REDIS,
           ProfileNames.GAME_COMMAND_REDIS, ProfileNames.GAME_QUERY_REDIS,
           ProfileNames.TABLE_COMMAND_REDIS, ProfileNames.TABLE_QUERY_REDIS })
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    StringRedisTemplate redisTemplate() {
        var redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, GameInListDTO> redisTemplateGameInList() {
        var redisTemplate = new RedisTemplate<String, GameInListDTO>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, TableEvent> redisTemplateTableEvent() {
        var redisTemplate = new RedisTemplate<String, TableEvent>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, GameEvent> redisTemplateGameEvent() {
        var redisTemplate = new RedisTemplate<String, GameEvent>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, OpenGameForUser> redisTemplateOpenGameForUser() {
        var redisTemplate = new RedisTemplate<String, OpenGameForUser>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

}
