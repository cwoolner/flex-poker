package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class AddPlayerCommand extends BaseCommand<TableCommandType>
        implements TableCommand {

    private static final TableCommandType TYPE = TableCommandType.AddPlayer;

    private final UUID tableId;

    private final UUID gameId;

    private final UUID playerId;

    private final int chips;

    public AddPlayerCommand(UUID tableId, UUID gameId, UUID playerId,
            int chips) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.chips = chips;
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

    public int getChips() {
        return chips;
    }

}
