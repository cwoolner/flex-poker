package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.aggregate.HandDealerState;
import com.flexpoker.table.command.framework.TableEvent;

public class RoundCompletedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final HandDealerState nextHandDealerState;

    public RoundCompletedEvent(UUID aggregateId, UUID gameId, UUID handId, HandDealerState nextHandDealerState) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.nextHandDealerState = nextHandDealerState;
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
    public HandDealerState getNextHandDealerState() {
        return nextHandDealerState;
    }

}
