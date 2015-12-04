package com.flexpoker.table.command.events;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class PotCreatedEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.PotCreated;

    private final UUID gameId;

    private final UUID handId;

    private final UUID potId;

    private final Set<UUID> playersInvolved;

    @JsonCreator
    public PotCreatedEvent(@JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "potId") UUID potId,
            @JsonProperty(value = "playersInvolved") Set<UUID> playersInvolved) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.handId = handId;
        this.potId = potId;
        this.playersInvolved = playersInvolved;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public UUID getPotId() {
        return potId;
    }

    public Set<UUID> getPlayersInvolved() {
        return new HashSet<>(playersInvolved);
    }

}
