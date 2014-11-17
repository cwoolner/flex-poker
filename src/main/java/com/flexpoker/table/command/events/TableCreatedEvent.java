package com.flexpoker.table.command.events;

import java.util.Map;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class TableCreatedEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.TableCreated;

    private final UUID gameId;

    private final int numberOfPlayersPerTable;

    private final Map<Integer, UUID> seatPositionToPlayerMap;

    public TableCreatedEvent(UUID aggregateId, int version, UUID gameId,
            int numberOfPlayersPerTable, Map<Integer, UUID> seatPositionToPlayerMap) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.seatPositionToPlayerMap = seatPositionToPlayerMap;
    }

    public UUID getGameId() {
        return gameId;
    }

    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

    public Map<Integer, UUID> getSeatPositionToPlayerMap() {
        return seatPositionToPlayerMap;
    }

}
