package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerFoldedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final UUID playerId;

    public PlayerFoldedEvent(UUID aggregateId, UUID gameId, UUID handId, UUID playerId) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.playerId = playerId;
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

}
