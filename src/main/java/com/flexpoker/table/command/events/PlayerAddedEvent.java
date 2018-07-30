package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerAddedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID playerId;

    private final int chipsInBack;

    private final int position;

    public PlayerAddedEvent(UUID aggregateId, UUID gameId, UUID playerId, int chipsInBack, int position) {
        super(aggregateId);
        this.gameId = gameId;
        this.playerId = playerId;
        this.chipsInBack = chipsInBack;
        this.position = position;
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

    @JsonProperty
    public int getChipsInBack() {
        return chipsInBack;
    }

    @JsonProperty
    public int getPosition() {
        return position;
    }

}
