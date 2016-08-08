package com.flexpoker.game.query.repository.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;

@Repository
public class OpenGameForPlayerInMemoryRepository implements OpenGameForPlayerRepository {

    private final Map<UUID, Map<UUID, OpenGameForUser>> openGameForUserMap;

    public OpenGameForPlayerInMemoryRepository() {
        openGameForUserMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<OpenGameForUser> fetchAllOpenGamesForPlayer(UUID playerId) {
        return openGameForUserMap.getOrDefault(playerId, Collections.emptyMap()).values().stream()
                .sorted(Comparator.comparingInt(OpenGameForUser::getOrdinal)).collect(Collectors.toList());
    }

    @Override
    public void deleteOpenGameForPlayer(UUID playerId, UUID gameId) {
        openGameForUserMap.get(playerId).remove(gameId);
    }

    @Override
    public void addOpenGameForUser(UUID playerId, UUID gameId, String gameName) {
        openGameForUserMap.putIfAbsent(playerId, new HashMap<>());
        OpenGameForUser openGameForUser = new OpenGameForUser(gameId, null, gameName, GameStage.REGISTERING,
                openGameForUserMap.get(playerId).size(), Collections.emptyList());
        openGameForUserMap.get(playerId).put(gameId, openGameForUser);
    }

    @Override
    public void changeGameStage(UUID playerId, UUID gameId, GameStage gameStage) {
        OpenGameForUser openGameForUser = openGameForUserMap.get(playerId).get(gameId);
        OpenGameForUser updatedOpenGameForUser = new OpenGameForUser(openGameForUser.getGameId(),
                openGameForUser.getMyTableId(), openGameForUser.getName(), gameStage, openGameForUser.getOrdinal(),
                openGameForUser.getViewingTables());
        openGameForUserMap.get(playerId).put(gameId, updatedOpenGameForUser);
    }

    @Override
    public void assignTableToOpenGame(UUID playerId, UUID gameId, UUID tableId) {
        OpenGameForUser openGameForUser = openGameForUserMap.get(playerId).get(gameId);
        OpenGameForUser updatedOpenGameForUser = new OpenGameForUser(openGameForUser.getGameId(), tableId,
                openGameForUser.getName(), openGameForUser.getGameStage(), openGameForUser.getOrdinal(),
                openGameForUser.getViewingTables());
        openGameForUserMap.get(playerId).put(gameId, updatedOpenGameForUser);
    }

}
