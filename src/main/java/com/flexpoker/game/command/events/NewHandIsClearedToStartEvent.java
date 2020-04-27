package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.aggregate.BlindAmounts;
import com.flexpoker.game.command.framework.GameEvent;

public class NewHandIsClearedToStartEvent extends BaseEvent implements GameEvent {

    private final UUID tableId;

    private final BlindAmounts blinds;

    @JsonCreator
    public NewHandIsClearedToStartEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "blinds") BlindAmounts blinds) {
        super(gameId);
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
