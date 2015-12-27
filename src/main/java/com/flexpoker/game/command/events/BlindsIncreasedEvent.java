package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class BlindsIncreasedEvent extends BaseEvent implements GameEvent {

    public BlindsIncreasedEvent(UUID aggregateId, int version) {
        super(aggregateId, version);
    }

}
