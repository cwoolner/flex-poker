package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class PauseCommand extends BaseCommand<TableCommandType>
        implements TableCommand {

    private static final TableCommandType TYPE = TableCommandType.Pause;

    private final UUID tableId;

    private final UUID gameId;

    public PauseCommand(UUID tableId, UUID gameId) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

}
