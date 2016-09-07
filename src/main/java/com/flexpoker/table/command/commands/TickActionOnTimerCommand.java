package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class TickActionOnTimerCommand extends BaseCommand<TableCommandType> implements
        TableCommand {

    private static final TableCommandType TYPE = TableCommandType.TickActionOnTimer;

    private final UUID tableId;

    private final UUID gameId;

    private final UUID handId;

    private final int number;

    public TickActionOnTimerCommand(UUID tableId, UUID gameId, UUID handId, int number) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.handId = handId;
        this.number = number;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public int getNumber() {
        return number;
    }

}
