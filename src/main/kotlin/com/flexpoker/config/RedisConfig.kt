package com.flexpoker.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.query.dto.OpenGameForUser
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.web.dto.OutgoingChatMessageDTO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.util.UUID

@Configuration
@Profile(
    ProfileNames.REDIS,
    ProfileNames.LOGIN_REDIS,
    ProfileNames.SIGNUP_REDIS,
    ProfileNames.GAME_COMMAND_REDIS,
    ProfileNames.GAME_QUERY_REDIS,
    ProfileNames.TABLE_COMMAND_REDIS,
    ProfileNames.TABLE_QUERY_REDIS
)
class RedisConfig {

    @Bean
    fun defaultObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.findAndRegisterModules()
        return objectMapper
    }

    @Bean
    @Primary
    fun redisStringTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, String::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateGameInList(connectionFactory: RedisConnectionFactory): RedisTemplate<String, GameInListDTO> {
        val redisTemplate = RedisTemplate<String, GameInListDTO>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, GameInListDTO::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateTableEvent(connectionFactory: RedisConnectionFactory): RedisTemplate<String, TableEvent> {
        val redisTemplate = RedisTemplate<String, TableEvent>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, TableEvent::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateGameEvent(connectionFactory: RedisConnectionFactory): RedisTemplate<String, GameEvent> {
        val redisTemplate = RedisTemplate<String, GameEvent>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, GameEvent::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateOpenGameForUser(connectionFactory: RedisConnectionFactory): RedisTemplate<String, OpenGameForUser> {
        val redisTemplate = RedisTemplate<String, OpenGameForUser>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, OpenGameForUser::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateFlopCards(connectionFactory: RedisConnectionFactory): RedisTemplate<String, FlopCards> {
        val redisTemplate = RedisTemplate<String, FlopCards>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, FlopCards::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateTurnCard(connectionFactory: RedisConnectionFactory): RedisTemplate<String, TurnCard> {
        val redisTemplate = RedisTemplate<String, TurnCard>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, TurnCard::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateRiverCard(connectionFactory: RedisConnectionFactory): RedisTemplate<String, RiverCard> {
        val redisTemplate = RedisTemplate<String, RiverCard>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, RiverCard::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplatePocketCards(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Map<UUID, PocketCards>> {
        val redisTemplate = RedisTemplate<String, Map<UUID, PocketCards>>()
        setupConnectionAndKeySerializerRedisTemplate(connectionFactory, redisTemplate)
        redisTemplate.hashKeySerializer = getJacksonSerializer(UUID::class.java)
        redisTemplate.hashValueSerializer = getJacksonSerializer(PocketCards::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateTableDTO(connectionFactory: RedisConnectionFactory): RedisTemplate<String, TableDTO> {
        val redisTemplate = RedisTemplate<String, TableDTO>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, TableDTO::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateChatMessageDTO(connectionFactory: RedisConnectionFactory): RedisTemplate<String, OutgoingChatMessageDTO> {
        val redisTemplate = RedisTemplate<String, OutgoingChatMessageDTO>()
        setupDefaultKeyValueRedisTemplate(connectionFactory, redisTemplate, OutgoingChatMessageDTO::class.java)
        return redisTemplate
    }

    private fun setupDefaultKeyValueRedisTemplate(
        connectionFactory: RedisConnectionFactory,
        redisTemplate: RedisTemplate<*, *>,
        clazz: Class<*>
    ) {
        setupConnectionAndKeySerializerRedisTemplate(connectionFactory, redisTemplate)
        redisTemplate.valueSerializer = getJacksonSerializer(clazz)
    }

    private fun setupConnectionAndKeySerializerRedisTemplate(
        connectionFactory: RedisConnectionFactory,
        redisTemplate: RedisTemplate<*, *>
    ) {
        redisTemplate.connectionFactory = connectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()
    }

    private fun getJacksonSerializer(clazz: Class<*>): Jackson2JsonRedisSerializer<*> {
        return Jackson2JsonRedisSerializer(defaultObjectMapper(), clazz)
    }

}