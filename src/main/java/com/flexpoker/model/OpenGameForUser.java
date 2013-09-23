package com.flexpoker.model;

import java.util.UUID;

public class OpenGameForUser {

    private final UUID gameId;

    private final String name;

    private GameStage gameStage;

    public OpenGameForUser(UUID gameId, String name, GameStage gameStage) {
        this.gameId = gameId;
        this.name = name;
        this.gameStage = gameStage;
    }

    public UUID getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    public GameStage getGameStage() {
        return gameStage;
    }
    
    public void changeGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

}
