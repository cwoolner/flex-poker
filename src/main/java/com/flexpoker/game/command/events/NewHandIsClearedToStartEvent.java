package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.game.command.events.dto.BlindAmountsDTO;
import com.flexpoker.game.command.framework.GameEvent;

public class NewHandIsClearedToStartEvent extends BaseGameEvent implements GameEvent {

    private final UUID tableId;

    private final BlindAmountsDTO blinds;

    @JsonCreator
    public NewHandIsClearedToStartEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "blinds") BlindAmountsDTO blinds) {
        super(gameId);
        this.tableId = tableId;
        this.blinds = blinds;
    }

    public UUID getTableId() {
        return tableId;
    }

    public BlindAmountsDTO getBlinds() {
        return blinds;
    }

}
