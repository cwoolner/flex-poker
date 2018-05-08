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
import com.flexpoker.table.command.framework.TableEvent;

@Profile("prod")
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
    public void save(TableEvent event) {
        redisTemplate.opsForList().rightPush(
                TABLE_EVENT_NAMESPACE + event.getAggregateId(), event);
    }

}
