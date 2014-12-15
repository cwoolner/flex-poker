package com.flexpoker.table.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class PlayerRaisedEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.PlayerRaised;

    private final UUID gameId;

    private final UUID handId;

    private final UUID playerId;

    private final int raiseToAmount;

    public PlayerRaisedEvent(UUID aggregateId, int version, UUID gameId, UUID handId,
            UUID playerId, int raiseToAmount) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.handId = handId;
        this.playerId = playerId;
        this.raiseToAmount = raiseToAmount;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getRaiseToAmount() {
        return raiseToAmount;
    }

}
