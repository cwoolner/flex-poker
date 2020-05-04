package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerAddedEvent extends BaseTableEvent implements TableEvent {

    private final UUID gameId;

    private final UUID playerId;

    private final int chipsInBack;

    private final int position;

    @JsonCreator
    public PlayerAddedEvent(
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "playerId") UUID playerId,
            @JsonProperty(value = "chipsInBack") int chipsInBack,
            @JsonProperty(value = "position") int position) {
        super(tableId);
        this.gameId = gameId;
        this.playerId = playerId;
        this.chipsInBack = chipsInBack;
        this.position = position;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getChipsInBack() {
        return chipsInBack;
    }

    public int getPosition() {
        return position;
    }

}
