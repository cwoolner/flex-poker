package com.flexpoker.game.command.events;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;

public class GameTablesCreatedAndPlayersAssociatedEvent extends BaseEvent<GameEventType>
        implements GameEvent {

    private static final GameEventType TYPE = GameEventType.GameTablesCreatedAndPlayersAssociated;

    private final Map<UUID, Set<UUID>> tableIdToPlayerIdsMap;

    public GameTablesCreatedAndPlayersAssociatedEvent(UUID aggregateId, int version,
            Map<UUID, Set<UUID>> tableIdToPlayerIdsMap) {
        super(aggregateId, version, TYPE);
        this.tableIdToPlayerIdsMap = tableIdToPlayerIdsMap;
    }

    public Map<UUID, Set<UUID>> getTableIdToPlayerIdsMap() {
        return tableIdToPlayerIdsMap;
    }

}
