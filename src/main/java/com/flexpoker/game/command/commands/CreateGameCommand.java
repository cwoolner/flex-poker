package com.flexpoker.game.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.game.command.framework.GameCommand;
import com.flexpoker.game.command.framework.GameCommandType;

public class CreateGameCommand extends BaseCommand<GameCommandType> implements
        GameCommand {

    private static final GameCommandType TYPE = GameCommandType.CreateGame;

    private final UUID createdByPlayerId;

    private final String gameName;

    private final int numberOfPlayers;

    private final int numberOfPlayersPerTable;

    public CreateGameCommand(String gameName, int numberOfPlayers,
            int numberOfPlayersPerTable, UUID createdByPlayerId) {
        super(TYPE);
        this.gameName = gameName;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.createdByPlayerId = createdByPlayerId;
    }

    public String getGameName() {
        return gameName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

    public UUID getCreatedByPlayerId() {
        return createdByPlayerId;
    }

}
