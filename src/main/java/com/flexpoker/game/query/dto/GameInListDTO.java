package com.flexpoker.game.query.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameInListDTO {

    private final UUID id;

    private final String name;

    private final String stage;

    private final int numberOfRegisteredPlayers;

    private final int maxNumberOfPlayers;

    private final int maxPlayersPerTable;

    private final int blindLevelIncreaseInMinutes;

    private final int blindTimerInSeconds;

    private final String createdBy;

    private final String createdOn;

    @JsonCreator
    public GameInListDTO(
            @JsonProperty(value = "id") UUID id,
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "stage") String stage,
            @JsonProperty(value = "numberOfRegisteredPlayers") int numberOfRegisteredPlayers,
            @JsonProperty(value = "maxNumberOfPlayers") int maxNumberOfPlayers,
            @JsonProperty(value = "maxPlayersPerTable") int maxPlayersPerTable,
            @JsonProperty(value = "blindLevelIncreaseInMinutes") int blindLevelIncreaseInMinutes,
            @JsonProperty(value = "blindTimerInSeconds") int blindTimerInSeconds,
            @JsonProperty(value = "createdBy") String createdBy,
            @JsonProperty(value = "createdOn") String createdOn) {
        this.id = id;
        this.name = name;
        this.stage = stage;
        this.numberOfRegisteredPlayers = numberOfRegisteredPlayers;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.maxPlayersPerTable = maxPlayersPerTable;
        this.blindLevelIncreaseInMinutes = blindLevelIncreaseInMinutes;
        this.blindTimerInSeconds = blindTimerInSeconds;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStage() {
        return stage;
    }

    public int getNumberOfRegisteredPlayers() {
        return numberOfRegisteredPlayers;
    }

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public int getMaxPlayersPerTable() {
        return maxPlayersPerTable;
    }

    public int getBlindLevelIncreaseInMinutes() {
        return blindLevelIncreaseInMinutes;
    }

    public int getBlindTimerInSeconds() {
        return blindTimerInSeconds;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

}
