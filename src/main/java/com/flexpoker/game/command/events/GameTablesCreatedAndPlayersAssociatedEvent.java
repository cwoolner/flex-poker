package com.flexpoker.game.command.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameTablesCreatedAndPlayersAssociatedEvent extends BaseEvent implements GameEvent {

    private final Map<UUID, Set<UUID>> tableIdToPlayerIdsMap;

    private final int numberOfPlayersPerTable;

    @JsonCreator
    public GameTablesCreatedAndPlayersAssociatedEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableIdToPlayerIdsMap") Map<UUID, Set<UUID>> tableIdToPlayerIdsMap,
            @JsonProperty(value = "numberOfPlayersPerTable") int numberOfPlayersPerTable) {
        super(gameId);
        this.tableIdToPlayerIdsMap = new HashMap<>(tableIdToPlayerIdsMap);
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
    }

    public Map<UUID, Set<UUID>> getTableIdToPlayerIdsMap() {
        return new HashMap<>(tableIdToPlayerIdsMap);
    }

    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

}
