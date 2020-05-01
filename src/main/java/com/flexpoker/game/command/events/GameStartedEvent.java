package com.flexpoker.game.command.events;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.events.dto.BlindScheduleDTO;
import com.flexpoker.game.command.framework.GameEvent;

public class GameStartedEvent extends BaseEvent implements GameEvent {

    private final Set<UUID> tableIds;

    private final BlindScheduleDTO blindScheduleDTO;

    @JsonCreator
    public GameStartedEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableIds") Set<UUID> tableIds,
            @JsonProperty(value = "blindSchedule") BlindScheduleDTO blindScheduleDTO) {
        super(gameId);
        this.tableIds = Collections.unmodifiableSet(tableIds);
        this.blindScheduleDTO = blindScheduleDTO;
    }

    public Set<UUID> getTableIds() {
        return tableIds;
    }

    public BlindScheduleDTO getBlindScheduleDTO() {
        return blindScheduleDTO;
    }

}
