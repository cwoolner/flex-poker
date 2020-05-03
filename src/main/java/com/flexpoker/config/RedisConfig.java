package com.flexpoker.config;

import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.web.dto.outgoing.TableDTO;

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

    @Bean
    RedisTemplate<String, FlopCards> redisTemplateFlopCards() {
        var redisTemplate = new RedisTemplate<String, FlopCards>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(
                new Jackson2JsonRedisSerializer<>(FlopCards.class));
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, TurnCard> redisTemplateTurnCard() {
        var redisTemplate = new RedisTemplate<String, TurnCard>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(
                new Jackson2JsonRedisSerializer<>(TurnCard.class));
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, RiverCard> redisTemplateRiverCard() {
        var redisTemplate = new RedisTemplate<String, RiverCard>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(
                new Jackson2JsonRedisSerializer<>(RiverCard.class));
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, Map<UUID, PocketCards>> redisTemplatePocketCards() {
        var redisTemplate = new RedisTemplate<String, Map<UUID, PocketCards>>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(UUID.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(PocketCards.class));
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, TableDTO> redisTemplateTableDTO() {
        var redisTemplate = new RedisTemplate<String, TableDTO>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(TableDTO.class));
        return redisTemplate;
    }

}
