package com.flexpoker.table.command.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class WinnersDeterminedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final Set<UUID> playersToShowCards;

    private final Map<UUID, Integer> playersToChipsWonMap;

    public WinnersDeterminedEvent(UUID aggregateId, UUID gameId, UUID handId, Set<UUID> playersToShowCards,
            Map<UUID, Integer> playersToChipsWonMap) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.playersToShowCards = new HashSet<>(playersToShowCards);
        this.playersToChipsWonMap = new HashMap<>(playersToChipsWonMap);
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
    public Set<UUID> getPlayersToShowCards() {
        return new HashSet<>(playersToShowCards);
    }

    @JsonProperty
    public Map<UUID, Integer> getPlayersToChipsWonMap() {
        return new HashMap<>(playersToChipsWonMap);
    }

}
