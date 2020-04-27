package com.flexpoker.game.command.events;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.aggregate.BlindSchedule;
import com.flexpoker.game.command.framework.GameEvent;

public class GameStartedEvent extends BaseEvent implements GameEvent {

    private final Set<UUID> tableIds;

    private final BlindSchedule blindSchedule;

    @JsonCreator
    public GameStartedEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableIds") Set<UUID> tableIds,
            @JsonProperty(value = "blindSchedule") BlindSchedule blindSchedule) {
        super(gameId);
        this.tableIds = new HashSet<>(tableIds);
        this.blindSchedule = blindSchedule;
    }

    public Set<UUID> getTableIds() {
        return new HashSet<>(tableIds);
    }

    public BlindSchedule getBlindSchedule() {
        return blindSchedule;
    }

}
