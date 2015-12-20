package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameMovedToStartingStageEvent extends BaseEvent implements
        GameEvent {

    public GameMovedToStartingStageEvent(UUID aggregateId, int version) {
        super(aggregateId, version);
    }

}
