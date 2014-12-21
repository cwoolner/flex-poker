package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class ExpireActionOnTimerCommand extends BaseCommand<TableCommandType> implements
        TableCommand {

    private static final TableCommandType TYPE = TableCommandType.ExpireActionOnTimer;

    private final UUID tableId;

    private final UUID gameId;

    private final UUID playerId;

    public ExpireActionOnTimerCommand(UUID tableId, UUID gameId, UUID playerId) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
