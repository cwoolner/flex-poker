package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TableResumedAfterBalancingEvent extends BaseEvent implements GameEvent {

    private final UUID tableId;

    public TableResumedAfterBalancingEvent(UUID aggregateId, int version, UUID tableId) {
        super(aggregateId, version);
        this.tableId = tableId;
    }

    public UUID getTableId() {
        return tableId;
    }

}
