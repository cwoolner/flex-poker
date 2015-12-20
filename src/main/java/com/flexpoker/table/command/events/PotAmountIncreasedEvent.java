package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PotAmountIncreasedEvent extends BaseEvent implements
        TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final UUID potId;

    private final int amountIncreased;

    @JsonCreator
    public PotAmountIncreasedEvent(@JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "potId") UUID potId,
            @JsonProperty(value = "amountIncreased") int amountIncreased) {
        super(aggregateId, version);
        this.gameId = gameId;
        this.handId = handId;
        this.potId = potId;
        this.amountIncreased = amountIncreased;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public UUID getPotId() {
        return potId;
    }

    public int getAmountIncreased() {
        return amountIncreased;
    }

}
