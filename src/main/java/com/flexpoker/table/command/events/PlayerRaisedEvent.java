package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerRaisedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final UUID playerId;

    private final int raiseToAmount;

    @JsonCreator
    public PlayerRaisedEvent(
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "playerId") UUID playerId,
            @JsonProperty(value = "raiseToAmount") int raiseToAmount) {
        super(tableId);
        this.gameId = gameId;
        this.handId = handId;
        this.playerId = playerId;
        this.raiseToAmount = raiseToAmount;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getRaiseToAmount() {
        return raiseToAmount;
    }

}
