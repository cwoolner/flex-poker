package com.flexpoker.game.command.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public interface GameEventRepository {

    List<GameEvent> fetchAll(UUID id);

    List<GameEvent> setEventVersionsAndSave(int basedOnVersion, List<GameEvent> events);

    GameCreatedEvent fetchGameCreatedEvent(UUID aggregateId);

}
