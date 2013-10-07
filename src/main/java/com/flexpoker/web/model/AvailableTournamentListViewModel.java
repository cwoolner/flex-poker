package com.flexpoker.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableTournamentListViewModel {

    @JsonProperty
    private final String name;
    
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
    
    public AvailableTournamentListViewModel(String name, int numberOfRegisteredPlayers,
            int maxNumberOfPlayers, int maxPlayersPerTable, String createdBy, String createdOn) {
        this.name = name;
        this.numberOfRegisteredPlayers = numberOfRegisteredPlayers;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.maxPlayersPerTable = maxPlayersPerTable;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

}
