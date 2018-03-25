package com.flexpoker.game.query.repository.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.repository.GameListRepository;

@Profile("prod")
@Repository
public class RedisGameListRepository implements GameListRepository {

    private static final String GAME_LIST_NAMESPACE = "game-list:";

    private final RedisTemplate<String, GameInListDTO> redisTemplate;

    @Inject
    public RedisGameListRepository(RedisTemplate<String, GameInListDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(
                GameInListDTO.class));
    }

    @Override
    public void saveNew(GameInListDTO gameInListDTO) {
        redisTemplate.opsForValue().set(GAME_LIST_NAMESPACE + gameInListDTO.getId(),
                gameInListDTO);
    }

    @Override
    public List<GameInListDTO> fetchAll() {
        Set<String> allKeys = redisTemplate.keys(GAME_LIST_NAMESPACE + "*");
        return redisTemplate.opsForValue().multiGet(allKeys);
    }

    @Override
    public void incrementRegisteredPlayers(UUID aggregateId) {
        GameInListDTO existingGameInListDTO = fetchById(aggregateId);
        GameInListDTO updatedGameInListDTO = new GameInListDTO(
                existingGameInListDTO.getId(), existingGameInListDTO.getName(),
                existingGameInListDTO.getStage(),
                existingGameInListDTO.getNumberOfRegisteredPlayers() + 1,
                existingGameInListDTO.getMaxNumberOfPlayers(),
                existingGameInListDTO.getMaxPlayersPerTable(),
                existingGameInListDTO.getBlindLevelIncreaseInMinutes(),
                existingGameInListDTO.getActionOnTimerInSeconds(),
                existingGameInListDTO.getCreatedBy(),
                existingGameInListDTO.getCreatedOn());
        removeGame(aggregateId);
        saveNew(updatedGameInListDTO);
    }

    @Override
    public String fetchGameName(UUID aggregateId) {
        GameInListDTO existingGameInListDTO = fetchById(aggregateId);
        return existingGameInListDTO.getName();
    }

    @Override
    public void changeGameStage(UUID aggregateId, GameStage gameStage) {
        GameInListDTO existingGameInListDTO = fetchById(aggregateId);
        GameInListDTO updatedGameInListDTO = new GameInListDTO(
                existingGameInListDTO.getId(), existingGameInListDTO.getName(),
                gameStage.toString(),
                existingGameInListDTO.getNumberOfRegisteredPlayers(),
                existingGameInListDTO.getMaxNumberOfPlayers(),
                existingGameInListDTO.getMaxPlayersPerTable(),
                existingGameInListDTO.getBlindLevelIncreaseInMinutes(),
                existingGameInListDTO.getActionOnTimerInSeconds(),
                existingGameInListDTO.getCreatedBy(),
                existingGameInListDTO.getCreatedOn());
        removeGame(aggregateId);
        saveNew(updatedGameInListDTO);
    }

    private GameInListDTO fetchById(UUID aggregateId) {
        return redisTemplate.opsForValue().get(GAME_LIST_NAMESPACE + aggregateId);
    }

    private void removeGame(UUID aggregateId) {
        redisTemplate.delete(GAME_LIST_NAMESPACE + aggregateId);
    }

}
