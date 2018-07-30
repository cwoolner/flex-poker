package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TablePausedForBalancingEvent extends BaseEvent implements GameEvent {

    private final UUID tableId;

    public TablePausedForBalancingEvent(UUID aggregateId, UUID tableId) {
        super(aggregateId);
        this.tableId = tableId;
    }

    public UUID getTableId() {
        return tableId;
    }

}
