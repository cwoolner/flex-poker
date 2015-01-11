package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;
import com.flexpoker.model.Blinds;

public class NewHandIsClearedToStartEvent extends BaseEvent<GameEventType> implements
        GameEvent {

    private static final GameEventType TYPE = GameEventType.NewHandIsClearedToStart;

    private final UUID tableId;

    private final Blinds blinds;

    public NewHandIsClearedToStartEvent(UUID aggregateId, int version, UUID tableId,
            Blinds blinds) {
        super(aggregateId, version, TYPE);
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
