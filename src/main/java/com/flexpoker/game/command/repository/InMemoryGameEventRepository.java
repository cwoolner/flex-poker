package com.flexpoker.game.command.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.GameCreatedEvent;
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
    public List<GameEvent> setEventVersionsAndSave(int basedOnVersion, List<GameEvent> events) {
        var aggregateId = events.get(0).getAggregateId();

        if (!gameEventMap.containsKey(aggregateId)) {
            gameEventMap.put(aggregateId, new ArrayList<>());
        }

        var existingEvents = gameEventMap.get(aggregateId);
        if (existingEvents.size() != basedOnVersion) {
            throw new FlexPokerException("events to save are based on a different version of the aggregate");
        }

        for (int i = 0; i < events.size(); i++) {
            events.get(i).setVersion(basedOnVersion + i + 1);
        }

        gameEventMap.get(aggregateId).addAll(events);

        return events;
    }

    @Override
    public GameCreatedEvent fetchGameCreatedEvent(UUID gameId) {
        var gameEvents = fetchAll(gameId);
        for (var gameEvent : gameEvents) {
            if (gameEvent.getClass() == GameCreatedEvent.class) {
                return (GameCreatedEvent) gameEvent;
            }
        }
        return null;
    }

}
