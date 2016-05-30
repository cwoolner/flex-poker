package com.flexpoker.game.command.commands;

import java.util.Map;
import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.game.command.framework.GameCommand;
import com.flexpoker.game.command.framework.GameCommandType;

public class AttemptToStartNewHandCommand extends BaseCommand<GameCommandType> implements
        GameCommand {

    private static final GameCommandType TYPE = GameCommandType.AttemptToStartNewHand;

    private final UUID aggregateId;

    private final UUID tableId;

    private final Map<UUID, Integer> playerToChipsAtTableMap;

    public AttemptToStartNewHandCommand(UUID aggregateId, UUID tableId,
            Map<UUID, Integer> playerToChipsAtTableMap) {
        super(TYPE);
        this.aggregateId = aggregateId;
        this.tableId = tableId;
        this.playerToChipsAtTableMap = playerToChipsAtTableMap;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public Map<UUID, Integer> getPlayerToChipsAtTableMap() {
        return playerToChipsAtTableMap;
    }

}
