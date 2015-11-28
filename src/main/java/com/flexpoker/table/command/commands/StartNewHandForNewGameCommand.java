package com.flexpoker.table.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

public class StartNewHandForNewGameCommand extends BaseCommand<TableCommandType>
        implements TableCommand {

    private static final TableCommandType TYPE = TableCommandType.StartNewHandForNewGame;

    private final UUID tableId;

    private final UUID gameId;

    private final int smallBlind;

    private final int bigBlind;

    public StartNewHandForNewGameCommand(UUID tableId, UUID gameId,
            int smallBlind, int bigBlind) {
        super(TYPE);
        this.tableId = tableId;
        this.gameId = gameId;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

}
