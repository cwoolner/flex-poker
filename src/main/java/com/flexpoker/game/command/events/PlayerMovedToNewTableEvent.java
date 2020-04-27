package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class PlayerMovedToNewTableEvent extends BaseEvent implements GameEvent {

    private final UUID fromTableId;

    private final UUID toTableId;

    private final UUID playerId;

    private final int chips;

    @JsonCreator
    public PlayerMovedToNewTableEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "fromTableId") UUID fromTableId,
            @JsonProperty(value = "toTableId") UUID toTableId,
            @JsonProperty(value = "playerId") UUID playerId,
            @JsonProperty(value = "chips") int chips) {
        super(gameId);
        this.fromTableId = fromTableId;
        this.toTableId = toTableId;
        this.playerId = playerId;
        this.chips = chips;
    }

    public UUID getFromTableId() {
        return fromTableId;
    }

    public UUID getToTableId() {
        return toTableId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getChips() {
        return chips;
    }

}
