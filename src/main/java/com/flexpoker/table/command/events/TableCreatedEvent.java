package com.flexpoker.table.command.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class TableCreatedEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final int numberOfPlayersPerTable;

    private final Map<Integer, UUID> seatPositionToPlayerMap;

    private final int startingNumberOfChips;

    public TableCreatedEvent(UUID aggregateId, UUID gameId, int numberOfPlayersPerTable,
            Map<Integer, UUID> seatPositionToPlayerMap, int startingNumberOfChips) {
        super(aggregateId);
        this.gameId = gameId;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.seatPositionToPlayerMap = new HashMap<>(seatPositionToPlayerMap);
        this.startingNumberOfChips = startingNumberOfChips;
    }

    @JsonProperty
    @Override
    public UUID getGameId() {
        return gameId;
    }

    @JsonProperty
    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

    @JsonProperty
    public Map<Integer, UUID> getSeatPositionToPlayerMap() {
        return new HashMap<>(seatPositionToPlayerMap);
    }

    @JsonProperty
    public int getStartingNumberOfChips() {
        return startingNumberOfChips;
    }

}
