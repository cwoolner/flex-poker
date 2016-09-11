package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class PlayerBustedGameEvent extends BaseEvent implements GameEvent {

    private final UUID playerId;

    public PlayerBustedGameEvent(UUID aggregateId, int version, UUID playerId) {
        super(aggregateId, version);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
