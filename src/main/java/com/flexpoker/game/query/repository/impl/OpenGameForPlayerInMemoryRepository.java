package com.flexpoker.game.query.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;

@Repository
public class OpenGameForPlayerInMemoryRepository implements OpenGameForPlayerRepository {

    private final Map<UUID, List<OpenGameForUser>> openGameForUserMap;

    public OpenGameForPlayerInMemoryRepository() {
        openGameForUserMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<OpenGameForUser> fetchAllOpenGamesForPlayer(UUID playerId) {
        return openGameForUserMap.getOrDefault(playerId, new ArrayList<>());
    }

    @Override
    public void deleteOpenGameForPlayer(UUID playerId, UUID gameId) {
        List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(playerId);
        OpenGameForUser openGameForUserToDelete = openGameForUserList
                .stream().filter(x -> x.getGameId().equals(gameId)).findAny()
                .orElseThrow(IllegalArgumentException::new);
        openGameForUserList.remove(openGameForUserToDelete);
    }

    @Override
    public void addOpenGameForUser(UUID playerId, OpenGameForUser openGameForUser) {
        openGameForUserMap.putIfAbsent(playerId, new ArrayList<>());
        openGameForUserMap.get(playerId).add(openGameForUser);
    }

    @Override
    public void setGameStage(UUID playerId, UUID gameId, GameStage gameStage) {
        List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(playerId);
        OpenGameForUser openGameForUser = openGameForUserList.stream()
                .filter(x -> x.getGameId().equals(gameId)).findAny()
                .orElseThrow(IllegalArgumentException::new);
        openGameForUser.changeGameStage(gameStage);
    }

}
