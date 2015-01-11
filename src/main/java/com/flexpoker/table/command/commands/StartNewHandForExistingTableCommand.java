package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.model.Blinds;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class StartNewHandForExistingTableCommand extends BaseCommand<TableCommandType>
        implements TableCommand {

    private static final TableCommandType TYPE = TableCommandType.StartNewHandForExistingTable;

    private final UUID tableId;

    private final UUID gameId;

    private final Blinds blinds;

    public StartNewHandForExistingTableCommand(UUID tableId, UUID gameId, Blinds blinds) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.blinds = blinds;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Blinds getBlinds() {
        return blinds;
    }

}
