package com.flexpoker.game.query.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.game.query.dto.GameStage;
import com.flexpoker.game.query.dto.OpenGameForUser;

public interface OpenGameForPlayerRepository {

    List<OpenGameForUser> fetchAllOpenGamesForPlayer(UUID playerId);

    void deleteOpenGameForPlayer(UUID playerId, UUID gameId);

    void addOpenGameForUser(UUID playerId, UUID gameId, String gameName);

    void changeGameStage(UUID playerId, UUID gameId, GameStage gameStage);

    void assignTableToOpenGame(UUID playerId, UUID gameId, UUID tableId);

}
