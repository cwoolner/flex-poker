package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;

public class GameMovedToStartingStageEvent extends BaseEvent<GameEventType> implements
        GameEvent {

    private static final GameEventType TYPE = GameEventType.GameMovedToStartingStage;

    public GameMovedToStartingStageEvent(UUID aggregateId, int version) {
        super(aggregateId, version, TYPE);
    }

}
