package com.flexpoker.table.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class PlayerFoldedEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.PlayerFolded;

    private final UUID gameId;

    private final UUID handId;

    private final UUID playerId;

    // private final int actionOnPosition;

    // private final HandDealerState handDealerState;

    // private final HandRoundState roundCompleted;

    // private final Map<UUID, Integer> chipsInBack;

    // private final Map<UUID, Integer> chipsInFrontMap;

    // private final Map<UUID, Integer> callAmountsMap;

    // private final Map<UUID, Integer> raiseToAmountsMap;

    // private final Set<UUID> playersToShowCardsMap;

    public PlayerFoldedEvent(UUID aggregateId, int version, UUID gameId, UUID handId,
            UUID playerId) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.handId = handId;
        this.playerId = playerId;
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

}
