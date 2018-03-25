package com.flexpoker.web.dto.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGameDTO {

    private final String name;

    private final int players;

    private final int playersPerTable;

    private final int numberOfMinutesBetweenBlindLevels;

    private final int numberOfSecondsForActionOnTimer;

    @JsonCreator
    public CreateGameDTO(@JsonProperty(value = "name") String name,
            @JsonProperty(value = "players") int players,
            @JsonProperty(value = "playersPerTable") int playersPerTable,
            @JsonProperty(value = "numberOfMinutesBetweenBlindLevels") int numberOfMinutesBetweenBlindLevels,
            @JsonProperty(value = "numberOfSecondsForActionOnTimer") int numberOfSecondsForActionOnTimer) {
        this.name = name;
        this.players = players;
        this.playersPerTable = playersPerTable;
        this.numberOfMinutesBetweenBlindLevels = numberOfMinutesBetweenBlindLevels;
        this.numberOfSecondsForActionOnTimer= numberOfSecondsForActionOnTimer;
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

    public int getNumberOfMinutesBetweenBlindLevels() {
        return numberOfMinutesBetweenBlindLevels;
    }

    public int getNumberOfSecondsForActionOnTimer() {
        return numberOfSecondsForActionOnTimer;
    }

}
