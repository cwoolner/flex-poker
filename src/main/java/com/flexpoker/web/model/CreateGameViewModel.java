package com.flexpoker.web.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGameViewModel {

    private final String name;

    private final Integer players;

    private final Integer playersPerTable;

    @JsonCreator
    public CreateGameViewModel(
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "players") Integer players,
            @JsonProperty(value = "playersPerTable") Integer playersPerTable) {
        this.name = name;
        this.players = players;
        this.playersPerTable = playersPerTable;
    }

    public String getName() {
        return name;
    }

    public Integer getPlayers() {
        return players;
    }

    public Integer getPlayersPerTable() {
        return playersPerTable;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
