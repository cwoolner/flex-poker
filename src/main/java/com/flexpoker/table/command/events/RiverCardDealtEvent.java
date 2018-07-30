package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class RiverCardDealtEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    public RiverCardDealtEvent(UUID aggregateId, UUID gameId, UUID handId) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
    }

    @JsonProperty
    @Override
    public UUID getGameId() {
        return gameId;
    }

    @JsonProperty
    public UUID getHandId() {
        return handId;
    }

}
