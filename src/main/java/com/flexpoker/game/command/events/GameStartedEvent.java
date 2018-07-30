package com.flexpoker.game.command.events;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.aggregate.BlindSchedule;
import com.flexpoker.game.command.framework.GameEvent;

public class GameStartedEvent extends BaseEvent implements GameEvent {

    private final Set<UUID> tableIds;

    private final BlindSchedule blindSchedule;

    public GameStartedEvent(UUID aggregateId, Set<UUID> tableIds, BlindSchedule blindSchedule) {
        super(aggregateId);
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
