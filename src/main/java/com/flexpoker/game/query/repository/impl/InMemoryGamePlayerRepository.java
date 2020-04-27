package com.flexpoker.game.query.repository.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.game.query.repository.GamePlayerRepository;

@Profile({ ProfileNames.DEFAULT, ProfileNames.GAME_QUERY_INMEMORY })
@Repository
public class InMemoryGamePlayerRepository implements GamePlayerRepository {

    private final Map<UUID, Set<UUID>> gameIdToPlayerId;

    public InMemoryGamePlayerRepository() {
        gameIdToPlayerId = new HashMap<>();
    }

    @Override
    public void addPlayerToGame(UUID playerId, UUID gameId) {
        gameIdToPlayerId.putIfAbsent(gameId, new HashSet<>());
        gameIdToPlayerId.get(gameId).add(playerId);
    }

    @Override
    public Set<UUID> fetchAllPlayerIdsForGame(UUID gameId) {
        return gameIdToPlayerId.get(gameId);
    }

}
