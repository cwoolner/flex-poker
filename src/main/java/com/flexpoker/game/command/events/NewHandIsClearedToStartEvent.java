package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.aggregate.BlindAmounts;
import com.flexpoker.game.command.framework.GameEvent;

public class NewHandIsClearedToStartEvent extends BaseEvent implements GameEvent {

    private final UUID tableId;

    private final BlindAmounts blinds;

    public NewHandIsClearedToStartEvent(UUID aggregateId, UUID tableId, BlindAmounts blinds) {
        super(aggregateId);
        this.tableId = tableId;
        this.blinds = blinds;
    }

    public UUID getTableId() {
        return tableId;
    }

    public BlindAmounts getBlinds() {
        return blinds;
    }

}
