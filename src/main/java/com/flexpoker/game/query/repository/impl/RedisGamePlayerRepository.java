package com.flexpoker.game.query.repository.impl;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.game.query.repository.GamePlayerRepository;

@Profile({ ProfileNames.REDIS, ProfileNames.GAME_QUERY_REDIS })
@Repository
public class RedisGamePlayerRepository implements GamePlayerRepository {

    private static final String GAME_PLAYER_NAMESPACE = "game-player:";

    private final RedisTemplate<String, String> redisTemplate;

    @Inject
    public RedisGamePlayerRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addPlayerToGame(UUID playerId, UUID gameId) {
        redisTemplate.opsForSet().add(redisKey(gameId), playerId.toString());
    }

    @Override
    public Set<UUID> fetchAllPlayerIdsForGame(UUID gameId) {
        return redisTemplate.opsForSet().members(redisKey(gameId))
                .stream()
                .map(x -> UUID.fromString(x))
                .collect(Collectors.toSet());
    }

    private String redisKey(UUID gameId) {
        return GAME_PLAYER_NAMESPACE + gameId;
    }

}
