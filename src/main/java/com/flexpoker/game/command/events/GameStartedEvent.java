package com.flexpoker.game.command.events;

import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;
import com.flexpoker.model.Blinds;

public class GameStartedEvent extends BaseEvent<GameEventType> implements GameEvent {

    private static final GameEventType TYPE = GameEventType.GameStarted;

    private final Set<UUID> tableIds;

    private final Blinds blinds;

    public GameStartedEvent(UUID aggregateId, int version, Set<UUID> tableIds,
            Blinds blinds) {
        super(aggregateId, version, TYPE);
        this.tableIds = tableIds;
        this.blinds = blinds;
    }

    public Set<UUID> getTableIds() {
        return tableIds;
    }

    public Blinds getBlinds() {
        return blinds;
    }

}
