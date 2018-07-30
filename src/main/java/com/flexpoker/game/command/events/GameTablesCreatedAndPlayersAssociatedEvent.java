package com.flexpoker.game.command.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameTablesCreatedAndPlayersAssociatedEvent extends BaseEvent implements GameEvent {

    private final Map<UUID, Set<UUID>> tableIdToPlayerIdsMap;

    private final int numberOfPlayersPerTable;

    public GameTablesCreatedAndPlayersAssociatedEvent(UUID aggregateId, Map<UUID, Set<UUID>> tableIdToPlayerIdsMap,
            int numberOfPlayersPerTable) {
        super(aggregateId);
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
