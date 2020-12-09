package com.flexpoker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.query.dto.TableDTO;
import com.flexpoker.web.dto.OutgoingChatMessageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;
import java.util.UUID;

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
    ObjectMapper defaultObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    @Primary
    RedisTemplate<String, String> redisStringTemplate() {
        var redisTemplate = new RedisTemplate<String, String>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, String.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, GameInListDTO> redisTemplateGameInList() {
        var redisTemplate = new RedisTemplate<String, GameInListDTO>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, GameInListDTO.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, TableEvent> redisTemplateTableEvent() {
        var redisTemplate = new RedisTemplate<String, TableEvent>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, TableEvent.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, GameEvent> redisTemplateGameEvent() {
        var redisTemplate = new RedisTemplate<String, GameEvent>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, GameEvent.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, OpenGameForUser> redisTemplateOpenGameForUser() {
        var redisTemplate = new RedisTemplate<String, OpenGameForUser>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, OpenGameForUser.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, FlopCards> redisTemplateFlopCards() {
        var redisTemplate = new RedisTemplate<String, FlopCards>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, FlopCards.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, TurnCard> redisTemplateTurnCard() {
        var redisTemplate = new RedisTemplate<String, TurnCard>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, TurnCard.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, RiverCard> redisTemplateRiverCard() {
        var redisTemplate = new RedisTemplate<String, RiverCard>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, RiverCard.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, Map<UUID, PocketCards>> redisTemplatePocketCards() {
        var redisTemplate = new RedisTemplate<String, Map<UUID, PocketCards>>();
        setupConnectionAndKeySerializerRedisTemplate(redisTemplate);
        redisTemplate.setHashKeySerializer(getJacksonSerializer(UUID.class));
        redisTemplate.setHashValueSerializer(getJacksonSerializer(PocketCards.class));
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, TableDTO> redisTemplateTableDTO() {
        var redisTemplate = new RedisTemplate<String, TableDTO>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, TableDTO.class);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, OutgoingChatMessageDTO> redisTemplateChatMessageDTO() {
        var redisTemplate = new RedisTemplate<String, OutgoingChatMessageDTO>();
        setupDefaultKeyValueRedisTemplate(redisTemplate, OutgoingChatMessageDTO.class);
        return redisTemplate;
    }

    private void setupDefaultKeyValueRedisTemplate(RedisTemplate<?, ?> redisTemplate, Class<?> clazz) {
        setupConnectionAndKeySerializerRedisTemplate(redisTemplate);
        redisTemplate.setValueSerializer(getJacksonSerializer(clazz));
    }

    private void setupConnectionAndKeySerializerRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    private Jackson2JsonRedisSerializer<?> getJacksonSerializer(Class<?> clazz) {
        var valueSerializer = new Jackson2JsonRedisSerializer<>(clazz);
        valueSerializer.setObjectMapper(defaultObjectMapper());
        return valueSerializer;
    }

}
