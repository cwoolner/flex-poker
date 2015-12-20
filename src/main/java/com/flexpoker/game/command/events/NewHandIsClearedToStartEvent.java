package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.aggregate.Blinds;
import com.flexpoker.game.command.framework.GameEvent;

public class NewHandIsClearedToStartEvent extends BaseEvent implements
        GameEvent {

    private final UUID tableId;

    private final Blinds blinds;

    public NewHandIsClearedToStartEvent(UUID aggregateId, int version, UUID tableId,
            Blinds blinds) {
        super(aggregateId, version);
        this.tableId = tableId;
        this.blinds = blinds;
    }

    public UUID getTableId() {
        return tableId;
    }

    public Blinds getBlinds() {
        return blinds;
    }

}
