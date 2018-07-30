package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameFinishedEvent extends BaseEvent implements GameEvent {

    public GameFinishedEvent(UUID aggregateId) {
        super(aggregateId);
    }

}
