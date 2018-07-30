package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerRemovedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID playerId;

    public PlayerRemovedEvent(UUID aggregateId, UUID gameId, UUID playerId) {
        super(aggregateId);
        this.gameId = gameId;
        this.playerId = playerId;
    }

    @JsonProperty
    @Override
    public UUID getGameId() {
        return gameId;
    }

    @JsonProperty
    public UUID getPlayerId() {
        return playerId;
    }

}
