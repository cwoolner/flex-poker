package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;

public class GameJoinedEvent extends BaseEvent<GameEventType> implements GameEvent {

    private static final GameEventType TYPE = GameEventType.GameJoined;

    private final UUID playerId;

    public GameJoinedEvent(UUID aggregateId, int version, UUID playerId) {
        super(aggregateId, version, TYPE);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
