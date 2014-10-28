package com.flexpoker.game.query.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;

public interface OpenGameForPlayerRepository {

    List<OpenGameForUser> fetchAllOpenGamesForPlayer(UUID playerId);

    void deleteOpenGameForPlayer(UUID playerId, UUID gameId);

    void addOpenGameForUser(UUID playerId, OpenGameForUser openGameForUser);

    void setGameStage(UUID playerId, UUID gameId, GameStage gameStage);

}
