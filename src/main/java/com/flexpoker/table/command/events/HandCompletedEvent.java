package com.flexpoker.table.command.events;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class HandCompletedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final Map<UUID, Integer> playerToChipsAtTableMap;

    @JsonCreator
    public HandCompletedEvent(@JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "playerToChipsAtTableMap") Map<UUID, Integer> playerToChipsAtTableMap) {
        super(aggregateId, version);
        this.gameId = gameId;
        this.handId = handId;
        this.playerToChipsAtTableMap = playerToChipsAtTableMap;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public Map<UUID, Integer> getPlayerToChipsAtTableMap() {
        return playerToChipsAtTableMap;
    }

}
