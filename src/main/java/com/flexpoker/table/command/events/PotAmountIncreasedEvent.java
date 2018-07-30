package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PotAmountIncreasedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final UUID potId;

    private final int amountIncreased;

    public PotAmountIncreasedEvent(UUID aggregateId, UUID gameId, UUID handId, UUID potId, int amountIncreased) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.potId = potId;
        this.amountIncreased = amountIncreased;
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

    @JsonProperty
    public UUID getPotId() {
        return potId;
    }

    @JsonProperty
    public int getAmountIncreased() {
        return amountIncreased;
    }

}
