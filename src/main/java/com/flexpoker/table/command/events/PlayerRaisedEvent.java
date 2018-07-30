package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerRaisedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final UUID playerId;

    private final int raiseToAmount;

    public PlayerRaisedEvent(UUID aggregateId, UUID gameId, UUID handId, UUID playerId, int raiseToAmount) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.playerId = playerId;
        this.raiseToAmount = raiseToAmount;
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
    public UUID getPlayerId() {
        return playerId;
    }

    @JsonProperty
    public int getRaiseToAmount() {
        return raiseToAmount;
    }

}
