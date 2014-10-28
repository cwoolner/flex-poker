package com.flexpoker.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGameViewModel {

    private final String name;

    private final int players;

    private final int playersPerTable;

    @JsonCreator
    public CreateGameViewModel(@JsonProperty(value = "name") String name,
            @JsonProperty(value = "players") int players,
            @JsonProperty(value = "playersPerTable") int playersPerTable) {
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
