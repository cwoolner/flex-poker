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
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "gameId") UUID gameId) {
        super(tableId);
        this.gameId = gameId;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

}
