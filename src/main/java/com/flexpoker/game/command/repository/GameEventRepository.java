package com.flexpoker.game.command.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public interface GameEventRepository {

    List<GameEvent> fetchAll(UUID id);

    void save(GameEvent event);

    GameCreatedEvent fetchGameCreatedEvent(UUID aggregateId);

}
