package com.flexpoker.table.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class PlayerCheckedEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.PlayerChecked;

    private final UUID gameId;

    private final UUID playerId;

    public PlayerCheckedEvent(UUID aggregateId, int version, UUID gameId, UUID playerId) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.playerId = playerId;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
