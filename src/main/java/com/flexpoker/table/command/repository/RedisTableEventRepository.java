package com.flexpoker.table.command.repository;

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
import com.flexpoker.table.command.framework.TableEvent;

@Profile({ ProfileNames.REDIS, ProfileNames.TABLE_COMMAND_REDIS })
@Repository
public class RedisTableEventRepository implements TableEventRepository {

    private static final String TABLE_EVENT_NAMESPACE = "table-event:";

    private final RedisTemplate<String, TableEvent> redisTemplate;

    @Inject
    public RedisTableEventRepository(RedisTemplate<String, TableEvent> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());

        var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        Jackson2JsonRedisSerializer<TableEvent> valueSerializer = new Jackson2JsonRedisSerializer<>(
                TableEvent.class);
        valueSerializer.setObjectMapper(objectMapper);

        this.redisTemplate.setValueSerializer(valueSerializer);
    }

    @Override
    public List<TableEvent> fetchAll(UUID id) {
        return redisTemplate.opsForList().range(TABLE_EVENT_NAMESPACE + id, 0,
                Long.MAX_VALUE);
    }

    @Override
    public List<TableEvent> setEventVersionsAndSave(int basedOnVersion, List<TableEvent> events) {
        var aggregateId = events.get(0).getAggregateId();

        var existingEvents = fetchAll(aggregateId);
        if (existingEvents.size() != basedOnVersion) {
            throw new FlexPokerException("events to save are based on a different version of the aggregate");
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).setVersion(basedOnVersion + i + 1);
        }

        redisTemplate.opsForList().rightPushAll(TABLE_EVENT_NAMESPACE + aggregateId, events);

        return events;
    }

}
