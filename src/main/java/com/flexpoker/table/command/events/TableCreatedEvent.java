package com.flexpoker.table.command.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class TableCreatedEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.TableCreated;

    private final UUID gameId;

    private final int numberOfPlayersPerTable;

    private final Map<Integer, UUID> seatPositionToPlayerMap;

    private final int startingNumberOfChips;

    @JsonCreator
    public TableCreatedEvent(
            @JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "numberOfPlayersPerTable") int numberOfPlayersPerTable,
            @JsonProperty(value = "seatPositionToPlayerMap") Map<Integer, UUID> seatPositionToPlayerMap,
            @JsonProperty(value = "startingNumberOfChips") int startingNumberOfChips) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.seatPositionToPlayerMap = seatPositionToPlayerMap;
        this.startingNumberOfChips = startingNumberOfChips;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

    public Map<Integer, UUID> getSeatPositionToPlayerMap() {
        return new HashMap<>(seatPositionToPlayerMap);
    }

    public int getStartingNumberOfChips() {
        return startingNumberOfChips;
    }

}
