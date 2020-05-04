package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.game.command.framework.GameEvent;

public class TableRemovedEvent extends BaseGameEvent implements GameEvent {

    private final UUID tableId;

    @JsonCreator
    public TableRemovedEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableId") UUID tableId) {
        super(gameId);
        this.tableId = tableId;
    }

    public UUID getTableId() {
        return tableId;
    }

}
