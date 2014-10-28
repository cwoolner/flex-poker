package com.flexpoker.game.command.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.game.command.framework.GameEvent;

@Repository
public class InMemoryGameEventRepository implements GameEventRepository {

    private final Map<UUID, List<GameEvent>> gameEventMap;

    public InMemoryGameEventRepository() {
        gameEventMap = new HashMap<>();
    }

    @Override
    public List<GameEvent> fetchAll(UUID id) {
        return gameEventMap.get(id);
    }

    @Override
    public void save(GameEvent event) {
        if (!gameEventMap.containsKey(event.getAggregateId())) {
            gameEventMap.put(event.getAggregateId(), new ArrayList<>());
        }
        gameEventMap.get(event.getAggregateId()).add(event);
    }

}
