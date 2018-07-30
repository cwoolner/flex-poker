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

    public HandCompletedEvent(UUID aggregateId, UUID gameId, UUID handId, Map<UUID, Integer> playerToChipsAtTableMap) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.playerToChipsAtTableMap = new HashMap<>(playerToChipsAtTableMap);
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
    public Map<UUID, Integer> getPlayerToChipsAtTableMap() {
        return new HashMap<>(playerToChipsAtTableMap);
    }

}
