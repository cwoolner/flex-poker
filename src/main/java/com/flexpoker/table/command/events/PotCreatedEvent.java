package com.flexpoker.table.command.events;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class PotCreatedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final UUID potId;

    private final Set<UUID> playersInvolved;

    public PotCreatedEvent(UUID aggregateId, UUID gameId, UUID handId, UUID potId, Set<UUID> playersInvolved) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.potId = potId;
        this.playersInvolved = new HashSet<>(playersInvolved);
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
    public UUID getPotId() {
        return potId;
    }

    @JsonProperty
    public Set<UUID> getPlayersInvolved() {
        return new HashSet<>(playersInvolved);
    }

}
