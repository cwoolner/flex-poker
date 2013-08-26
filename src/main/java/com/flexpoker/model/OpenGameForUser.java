package com.flexpoker.model;

public class OpenGameForUser {

    // private final UUID gameId;

    private final Integer gameId;

    private final String name;

    private GameStage gameStage;

    public OpenGameForUser(Integer gameId, String name, GameStage gameStage) {
        this.gameId = gameId;
        this.name = name;
        this.gameStage = gameStage;
    }

    public Integer getGameId() {
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
