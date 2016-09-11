package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerBustedTableEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID playerId;

    @JsonCreator
    public PlayerBustedTableEvent(
            @JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "playerId") UUID playerId) {
        super(aggregateId, version);
        this.gameId = gameId;
        this.playerId = playerId;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
