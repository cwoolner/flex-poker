package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class TableResumedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    @JsonCreator
    public TableResumedEvent(
            @JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId) {
        super(aggregateId, version);
        this.gameId = gameId;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

}
