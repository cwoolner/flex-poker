package com.flexpoker.table.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.table.command.framework.TableEvent;

public class PlayerBustedTableEvent extends BaseTableEvent implements TableEvent {

    private final UUID gameId;

    private final UUID playerId;

    @JsonCreator
    public PlayerBustedTableEvent(
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "playerId") UUID playerId) {
        super(tableId);
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
