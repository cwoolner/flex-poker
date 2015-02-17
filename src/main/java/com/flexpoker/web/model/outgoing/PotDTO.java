package com.flexpoker.web.model.outgoing;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PotDTO {

    @JsonProperty
    private final Set<String> seats;

    @JsonProperty
    private final int amount;

    @JsonProperty
    private final boolean open;

    @JsonProperty
    private final Set<String> winners;

    public PotDTO(Set<String> seats, int amount, boolean open, Set<String> winners) {
        this.seats = seats;
        this.amount = amount;
        this.open = open;
        this.winners = winners;
    }

    public Set<String> getSeats() {
        return seats;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isOpen() {
        return open;
    }

    public Set<String> getWinners() {
        return winners;
    }

}
