package com.flexpoker.game.query.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenGameForUser {

    private final UUID gameId;

    private final UUID myTableId;

    private final String name;

    private final GameStage gameStage;

    private final int ordinal;

    private final List<String> viewingTables;

    @JsonCreator
    public OpenGameForUser(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "myTableId") UUID myTableId,
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "gameStage") GameStage gameStage,
            @JsonProperty(value = "ordinal") int ordinal,
            @JsonProperty(value = "viewingTables") List<String> viewingTables) {
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
