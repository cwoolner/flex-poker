package com.flexpoker.game.query.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameInListDTO {

    @JsonProperty
    private final UUID id;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String stage;

    @JsonProperty
    private final int numberOfRegisteredPlayers;

    @JsonProperty
    private final int maxNumberOfPlayers;

    @JsonProperty
    private final int maxPlayersPerTable;

    @JsonProperty
    private final String createdBy;

    @JsonProperty
    private final String createdOn;

    public GameInListDTO(UUID id, String name, String stage,
            int numberOfRegisteredPlayers, int maxNumberOfPlayers,
            int maxPlayersPerTable, String createdBy, String createdOn) {
        this.id = id;
        this.name = name;
        this.stage = stage;
        this.numberOfRegisteredPlayers = numberOfRegisteredPlayers;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.maxPlayersPerTable = maxPlayersPerTable;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

}
