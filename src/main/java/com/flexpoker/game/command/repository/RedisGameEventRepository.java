package com.flexpoker.game.command.repository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexpoker.config.ProfileNames;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

@Profile({ ProfileNames.REDIS, ProfileNames.TABLE_COMMAND_REDIS })
@Repository
public class RedisGameEventRepository implements GameEventRepository {

    private static final String GAME_EVENT_NAMESPACE = "game-event:";

    private final RedisTemplate<String, GameEvent> redisTemplate;

    @Inject
    public RedisGameEventRepository(RedisTemplate<String, GameEvent> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());

        var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        var valueSerializer = new Jackson2JsonRedisSerializer<>(GameEvent.class);
        valueSerializer.setObjectMapper(objectMapper);

        this.redisTemplate.setValueSerializer(valueSerializer);
    }

    @Override
    public List<GameEvent> fetchAll(UUID id) {
        return redisTemplate.opsForList().range(GAME_EVENT_NAMESPACE + id, 0,
                Long.MAX_VALUE);
    }

    @Override
    public List<GameEvent> setEventVersionsAndSave(int basedOnVersion, List<GameEvent> events) {
        var aggregateId = events.get(0).getAggregateId();

        var existingEvents = fetchAll(aggregateId);
        if (existingEvents.size() != basedOnVersion) {
            throw new FlexPokerException("events to save are based on a different version of the aggregate");
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).setVersion(basedOnVersion + i + 1);
        }

        redisTemplate.opsForList().rightPushAll(GAME_EVENT_NAMESPACE + aggregateId, events);

        return events;
    }

    @Override
    public GameCreatedEvent fetchGameCreatedEvent(UUID gameId) {
        var gameEvents = fetchAll(gameId);
        for (var gameEvent : gameEvents) {
            if (gameEvent.getClass() == GameCreatedEvent.class) {
                return (GameCreatedEvent) gameEvent;
            }
        }
        return null;
    }

}
