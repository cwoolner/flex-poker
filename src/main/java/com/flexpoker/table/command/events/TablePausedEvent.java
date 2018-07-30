package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class TablePausedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    public TablePausedEvent(UUID aggregateId, UUID gameId) {
        super(aggregateId);
        this.gameId = gameId;
    }

    @JsonProperty
    @Override
    public UUID getGameId() {
        return gameId;
    }

}
