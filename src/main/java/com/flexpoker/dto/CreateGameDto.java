package com.flexpoker.dto;

public class CreateGameDto {

    private final String name;

    private final int players;

    private final int playersPerTable;

    public CreateGameDto(String name, int players, int playersPerTable) {
        this.name = name;
        this.players = players;
        this.playersPerTable = playersPerTable;
    }

    public String getName() {
        return name;
    }

    public int getPlayers() {
        return players;
    }

    public int getPlayersPerTable() {
        return playersPerTable;
    }

}
