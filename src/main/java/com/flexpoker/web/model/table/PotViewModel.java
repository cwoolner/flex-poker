package com.flexpoker.web.model.table;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PotViewModel {

    @JsonProperty
    private Set<String> seats;

    @JsonProperty
    private int amount;

    @JsonProperty
    private boolean open;

    @JsonProperty
    private Set<String> winners;
    
    public PotViewModel(Set<String> seats, int amount, boolean open, Set<String> winners) {
        this.seats = seats;
        this.amount = amount;
        this.open = open;
        this.winners = winners;
    }

}
