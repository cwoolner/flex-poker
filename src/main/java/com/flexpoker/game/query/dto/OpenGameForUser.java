package com.flexpoker.game.query.dto;

import java.util.List;
import java.util.UUID;

public class OpenGameForUser {

    private final UUID gameId;

    private final UUID myTableId;

    private final String name;

    private final GameStage gameStage;

    private final int ordinal;

    private final List<String> viewingTables;

    public OpenGameForUser(UUID gameId, UUID myTableId, String name, GameStage gameStage, int ordinal,
            List<String> viewingTables) {
        this.gameId = gameId;
        this.myTableId = myTableId;
        this.name = name;
        this.gameStage = gameStage;
        this.ordinal = ordinal;
        this.viewingTables = viewingTables;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getMyTableId() {
        return myTableId;
    }

    public String getName() {
        return name;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public List<String> getViewingTables() {
        return viewingTables;
    }

}
