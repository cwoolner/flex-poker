package com.flexpoker.game.command.events;

import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;

public class GameStartedEvent extends BaseEvent<GameEventType> implements GameEvent {

    private static final GameEventType TYPE = GameEventType.GameStarted;

    private final Set<UUID> tableIds;

    public GameStartedEvent(UUID aggregateId, int version, Set<UUID> tableIds) {
        super(aggregateId, version, TYPE);
        this.tableIds = tableIds;
    }

    public Set<UUID> getTableIds() {
        return tableIds;
    }

}
