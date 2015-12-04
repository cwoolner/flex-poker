package com.flexpoker.table.command.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class WinnersDeterminedEvent extends BaseEvent<TableEventType> implements
        TableEvent {

    private static final TableEventType TYPE = TableEventType.WinnersDetermined;

    private final UUID gameId;

    private final UUID handId;

    private final Set<UUID> playersToShowCards;

    private final Map<UUID, Integer> playersToChipsWonMap;

    @JsonCreator
    public WinnersDeterminedEvent(
            @JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "playersToShowCards") Set<UUID> playersToShowCards,
            @JsonProperty(value = "playersToChipsWonMap") Map<UUID, Integer> playersToChipsWonMap) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.handId = handId;
        this.playersToShowCards = playersToShowCards;
        this.playersToChipsWonMap = playersToChipsWonMap;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public Set<UUID> getPlayersToShowCards() {
        return new HashSet<>(playersToShowCards);
    }

    public Map<UUID, Integer> getPlayersToChipsWonMap() {
        return new HashMap<>(playersToChipsWonMap);
    }

}
