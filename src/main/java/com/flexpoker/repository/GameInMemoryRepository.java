package com.flexpoker.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.Game;
import com.flexpoker.repository.api.GameRepository;

@Repository
public class GameInMemoryRepository implements GameRepository {

    private final Map<UUID, Game> gameMap;

    public GameInMemoryRepository() {
        gameMap = new HashMap<>();
    }

    @Override
    public Game findById(UUID id) {
        return gameMap.get(id);
    }

}
