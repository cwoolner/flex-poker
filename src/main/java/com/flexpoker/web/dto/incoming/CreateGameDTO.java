package com.flexpoker.web.dto.incoming;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGameDTO {

    private final String name;

    private final int players;

    private final int playersPerTable;

    private final int numberOfMinutesBetweenBlindLevels;

    private final int numberOfSecondsForBlindTimer;

    @JsonCreator
    public CreateGameDTO(@JsonProperty(value = "name") String name,
            @JsonProperty(value = "players") int players,
            @JsonProperty(value = "playersPerTable") int playersPerTable,
            @JsonProperty(value = "numberOfMinutesBetweenBlindLevels") int numberOfMinutesBetweenBlindLevels,
            @JsonProperty(value = "numberOfSecondsForBlindTimer") int numberOfSecondsForBlindTimer) {
        this.name = name;
        this.players = players;
        this.playersPerTable = playersPerTable;
        this.numberOfMinutesBetweenBlindLevels = numberOfMinutesBetweenBlindLevels;
        this.numberOfSecondsForBlindTimer= numberOfSecondsForBlindTimer;
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

    public int getNumberOfSecondsForBlindTimer() {
        return numberOfSecondsForBlindTimer;
    }

}
