package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class ResumeCommand extends BaseCommand<TableCommandType>
        implements TableCommand {

    private static final TableCommandType TYPE = TableCommandType.Resume;

    private final UUID tableId;

    private final UUID gameId;

    public ResumeCommand(UUID tableId, UUID gameId) {
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
