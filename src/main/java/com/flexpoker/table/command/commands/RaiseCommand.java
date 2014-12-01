package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class RaiseCommand extends BaseCommand<TableCommandType> implements TableCommand {

    private static final TableCommandType TYPE = TableCommandType.Raise;

    private final UUID tableId;

    private final UUID gameId;

    private final UUID playerId;

    private final int raiseToAmount;

    public RaiseCommand(UUID tableId, UUID gameId, UUID playerId, int raiseToAmount) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.raiseToAmount = raiseToAmount;
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

    public int getRaiseToAmount() {
        return raiseToAmount;
    }

}
