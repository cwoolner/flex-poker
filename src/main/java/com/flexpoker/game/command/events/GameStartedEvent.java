package com.flexpoker.game.command.events;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.aggregate.Blinds;
import com.flexpoker.game.command.framework.GameEvent;

public class GameStartedEvent extends BaseEvent implements GameEvent {

    private final Set<UUID> tableIds;

    private final Blinds blinds;

    public GameStartedEvent(UUID aggregateId, int version, Set<UUID> tableIds,
            Blinds blinds) {
        super(aggregateId, version);
        this.tableIds = tableIds;
        this.blinds = blinds;
    }

    public Set<UUID> getTableIds() {
        return new HashSet<>(tableIds);
    }

    public Blinds getBlinds() {
        return blinds;
    }

}
