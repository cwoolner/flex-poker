package com.flexpoker.game.query.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;

@Repository
public class OpenGameForPlayerInMemoryRepository implements OpenGameForPlayerRepository {

    private final Map<UUID, List<OpenGameForUser>> openGameForUserMap;

    public OpenGameForPlayerInMemoryRepository() {
        openGameForUserMap = new HashMap<>();
    }

    @Override
    public List<OpenGameForUser> fetchAllOpenGamesForPlayer(UUID playerId) {
        synchronized (openGameForUserMap) {
            if (!openGameForUserMap.containsKey(playerId)) {
                openGameForUserMap.put(playerId, new ArrayList<OpenGameForUser>());
            }
            return openGameForUserMap.get(playerId);
        }
    }

    @Override
    public void deleteOpenGameForPlayer(final UUID playerId, final UUID gameId) {
        synchronized (openGameForUserMap) {
            List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(playerId);
            OpenGameForUser openGameForUserToDelete = openGameForUserList
                    .stream().filter(x -> x.getGameId().equals(gameId)).findAny()
                    .orElseThrow(IllegalArgumentException::new);
            openGameForUserList.remove(openGameForUserToDelete);
        }
    }

    @Override
    public void addOpenGameForUser(UUID playerId, OpenGameForUser openGameForUser) {
        synchronized (openGameForUserMap) {
            if (!openGameForUserMap.containsKey(playerId)) {
                openGameForUserMap.put(playerId, new ArrayList<OpenGameForUser>());
            }
            List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(playerId);
            openGameForUserList.add(openGameForUser);
        }
    }

    @Override
    public void setGameStage(UUID playerId, final UUID gameId, GameStage gameStage) {
        synchronized (openGameForUserMap) {
            List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(playerId);
            OpenGameForUser openGameForUser = openGameForUserList.stream()
                    .filter(x -> x.getGameId().equals(gameId)).findAny()
                    .orElseThrow(IllegalArgumentException::new);
            openGameForUser.changeGameStage(gameStage);
        }
    }

}
