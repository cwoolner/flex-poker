package com.flexpoker.game.query.repository;

import java.util.Set;
import java.util.UUID;

public interface GamePlayerRepository {

    void addPlayerToGame(UUID playerId, UUID gameId);

    Set<UUID> fetchAllPlayerIdsForGame(UUID gameId);

}
