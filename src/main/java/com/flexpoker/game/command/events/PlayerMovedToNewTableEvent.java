package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class PlayerMovedToNewTableEvent extends BaseEvent implements GameEvent {

    private final UUID fromTableId;

    private final UUID toTableId;

    private final UUID playerId;

    public PlayerMovedToNewTableEvent(UUID aggregateId, int version,
            UUID fromTableId, UUID toTableId, UUID playerId) {
        super(aggregateId, version);
        this.fromTableId = fromTableId;
        this.toTableId = toTableId;
        this.playerId = playerId;
    }

    public UUID getFromTableId() {
        return fromTableId;
    }

    public UUID getToTableId() {
        return toTableId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
