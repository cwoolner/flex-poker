package com.flexpoker.game.query.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.GameStage;

public interface GameListRepository {

    void saveNew(GameInListDTO gameInListDTO);

    List<GameInListDTO> fetchAll();

    void incrementRegisteredPlayers(UUID aggregateId);

    String fetchGameName(UUID aggregateId);

    void changeGameStage(UUID aggregateId, GameStage gameStage);

}
