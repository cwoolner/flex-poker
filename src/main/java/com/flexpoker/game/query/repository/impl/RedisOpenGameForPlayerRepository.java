package com.flexpoker.game.query.repository.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;

@Profile({ ProfileNames.REDIS, ProfileNames.GAME_QUERY_REDIS })
@Repository
public class RedisOpenGameForPlayerRepository implements OpenGameForPlayerRepository {

    private static final String OPEN_GAME_FOR_PLAYER_NAMESPACE = "open-game-for-player:";

    private RedisTemplate<String, OpenGameForUser> redisTemplate;

    @Inject
    public RedisOpenGameForPlayerRepository(RedisTemplate<String, OpenGameForUser> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<OpenGameForUser> fetchAllOpenGamesForPlayer(UUID playerId) {
        return redisTemplate.opsForList().range(redisKey(playerId), 0, Long.MAX_VALUE)
                .stream()
                .sorted(Comparator.comparingInt(OpenGameForUser::getOrdinal)).collect(Collectors.toList());
    }

    @Override
    public void deleteOpenGameForPlayer(UUID playerId, UUID gameId) {
        var openGameForPlayer = fetchOpenGameForPlayer(playerId, gameId);
        redisTemplate.opsForList().remove(redisKey(playerId), 1, openGameForPlayer);

        var removedOrdinal = openGameForPlayer.getOrdinal();
        var gamesToDecrementOrdinal = fetchAllOpenGamesForPlayer(playerId)
                .stream()
                .filter(x -> x.getOrdinal() > removedOrdinal)
                .map(x -> new OpenGameForUser(x.getGameId(), x.getMyTableId(), x.getName(),
                        x.getGameStage(), x.getOrdinal() - 1, x.getViewingTables()))
                .collect(Collectors.toList());
        for (var decrementedOrdinal : gamesToDecrementOrdinal) {
            updateOpenGameForPlayer(playerId, decrementedOrdinal);
        }
    }

    @Override
    public void addOpenGameForUser(UUID playerId, UUID gameId, String gameName) {
        if (fetchOpenGameForPlayer(playerId, gameId) != null) {
            throw new IllegalStateException("gameId: " + gameId + " already exists for playerId: " + playerId);
        }

        var openGamesForPlayer = fetchAllOpenGamesForPlayer(playerId);
        var openGameForUser = new OpenGameForUser(gameId, null, gameName, GameStage.REGISTERING,
                openGamesForPlayer.size(), Collections.emptyList());
        redisTemplate.opsForList().rightPush(redisKey(playerId), openGameForUser);
    }

    @Override
    public void changeGameStage(UUID playerId, UUID gameId, GameStage gameStage) {
        var openGameForPlayer = fetchOpenGameForPlayer(playerId, gameId);
        var updatedOpenGameForPlayer = new OpenGameForUser(openGameForPlayer.getGameId(),
                openGameForPlayer.getMyTableId(), openGameForPlayer.getName(), gameStage,
                openGameForPlayer.getOrdinal(), openGameForPlayer.getViewingTables());
        updateOpenGameForPlayer(playerId, updatedOpenGameForPlayer);
    }

    @Override
    public void assignTableToOpenGame(UUID playerId, UUID gameId, UUID tableId) {
        var openGameForPlayer = fetchOpenGameForPlayer(playerId, gameId);
        var updatedOpenGameForPlayer = new OpenGameForUser(openGameForPlayer.getGameId(), tableId,
                openGameForPlayer.getName(), openGameForPlayer.getGameStage(), openGameForPlayer.getOrdinal(),
                openGameForPlayer.getViewingTables());
        updateOpenGameForPlayer(playerId, updatedOpenGameForPlayer);
    }

    private String redisKey(UUID playerId) {
        return OPEN_GAME_FOR_PLAYER_NAMESPACE + playerId;
    }

    private OpenGameForUser fetchOpenGameForPlayer(UUID playerId, UUID gameId) {
        return fetchAllOpenGamesForPlayer(playerId)
                .stream()
                .filter(x -> x.getGameId().equals(gameId))
                .findFirst()
                .orElse(null);
    }

    private void updateOpenGameForPlayer(UUID playerId, OpenGameForUser openGameForPlayer) {
        var openGames = redisTemplate.opsForList().range(redisKey(playerId), 0, Long.MAX_VALUE);
        var index = IntStream.range(0, openGames.size())
                .filter(x -> openGames.get(x).getGameId().equals(openGameForPlayer.getGameId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("gameId: " + openGameForPlayer.getGameId()
                        + " is not in the list to update for playerId: " + playerId));
        redisTemplate.opsForList().set(redisKey(playerId), index, openGameForPlayer);
    }

}
