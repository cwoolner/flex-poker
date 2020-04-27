package com.flexpoker.table.command.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class HandCompletedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final Map<UUID, Integer> playerToChipsAtTableMap;

    public HandCompletedEvent(
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "playerToChipsAtTableMap") Map<UUID, Integer> playerToChipsAtTableMap) {
        super(tableId);
        this.gameId = gameId;
        this.handId = handId;
        this.playerToChipsAtTableMap = new HashMap<>(playerToChipsAtTableMap);
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public Map<UUID, Integer> getPlayerToChipsAtTableMap() {
        return new HashMap<>(playerToChipsAtTableMap);
    }

}
