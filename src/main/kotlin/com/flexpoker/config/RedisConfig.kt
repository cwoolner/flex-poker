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
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
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
    fun jedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory(RedisStandaloneConfiguration("localhost", 6379))
    }

    @Bean
    fun defaultObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.findAndRegisterModules()
        return objectMapper
    }

    @Bean
    @Primary
    fun redisStringTemplate(): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, String::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateGameInList(): RedisTemplate<String, GameInListDTO> {
        val redisTemplate = RedisTemplate<String, GameInListDTO>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, GameInListDTO::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateTableEvent(): RedisTemplate<String, TableEvent> {
        val redisTemplate = RedisTemplate<String, TableEvent>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, TableEvent::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateGameEvent(): RedisTemplate<String, GameEvent> {
        val redisTemplate = RedisTemplate<String, GameEvent>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, GameEvent::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateOpenGameForUser(): RedisTemplate<String, OpenGameForUser> {
        val redisTemplate = RedisTemplate<String, OpenGameForUser>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, OpenGameForUser::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateFlopCards(): RedisTemplate<String, FlopCards> {
        val redisTemplate = RedisTemplate<String, FlopCards>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, FlopCards::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateTurnCard(): RedisTemplate<String, TurnCard> {
        val redisTemplate = RedisTemplate<String, TurnCard>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, TurnCard::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateRiverCard(): RedisTemplate<String, RiverCard> {
        val redisTemplate = RedisTemplate<String, RiverCard>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, RiverCard::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplatePocketCards(): RedisTemplate<String, Map<UUID, PocketCards>> {
        val redisTemplate = RedisTemplate<String, Map<UUID, PocketCards>>()
        setupConnectionAndKeySerializerRedisTemplate(redisTemplate)
        redisTemplate.hashKeySerializer = getJacksonSerializer(UUID::class.java)
        redisTemplate.hashValueSerializer = getJacksonSerializer(PocketCards::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateTableDTO(): RedisTemplate<String, TableDTO> {
        val redisTemplate = RedisTemplate<String, TableDTO>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, TableDTO::class.java)
        return redisTemplate
    }

    @Bean
    fun redisTemplateChatMessageDTO(): RedisTemplate<String, OutgoingChatMessageDTO> {
        val redisTemplate = RedisTemplate<String, OutgoingChatMessageDTO>()
        setupDefaultKeyValueRedisTemplate(redisTemplate, OutgoingChatMessageDTO::class.java)
        return redisTemplate
    }

    private fun setupDefaultKeyValueRedisTemplate(redisTemplate: RedisTemplate<*, *>, clazz: Class<*>) {
        setupConnectionAndKeySerializerRedisTemplate(redisTemplate)
        redisTemplate.valueSerializer = getJacksonSerializer(clazz)
    }

    private fun setupConnectionAndKeySerializerRedisTemplate(redisTemplate: RedisTemplate<*, *>) {
        redisTemplate.connectionFactory = jedisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
    }

    private fun getJacksonSerializer(clazz: Class<*>): Jackson2JsonRedisSerializer<*> {
        return Jackson2JsonRedisSerializer(defaultObjectMapper(), clazz)
    }

}