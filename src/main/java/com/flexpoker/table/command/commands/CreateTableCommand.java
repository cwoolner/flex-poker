package com.flexpoker.table.command.commands;

import java.util.Set;
import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class CreateTableCommand extends BaseCommand<TableCommandType> implements
        TableCommand {

    private static final TableCommandType TYPE = TableCommandType.CreateTable;

    private final UUID tableId;

    private final UUID gameId;

    private final Set<UUID> playerIds;

    private final int numberOfPlayersPerTable;

    public CreateTableCommand(UUID tableId, UUID gameId, Set<UUID> playerIds,
            int numberOfPlayersPerTable) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.playerIds = playerIds;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Set<UUID> getPlayerIds() {
        return playerIds;
    }

    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

}
