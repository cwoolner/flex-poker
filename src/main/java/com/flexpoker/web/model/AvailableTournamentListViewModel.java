package com.flexpoker.web.model;

public class AvailableTournamentListViewModel {

    private final String name;
    
    private final int numberOfRegisteredPlayers;
    
    private final int maxNumberOfPlayers;

    private final int maxPlayersPerTable;

    private final String createdBy;

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

    public String getName() {
        return name;
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
