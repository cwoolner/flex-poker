package com.flexpoker.web.dto.outgoing;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PotDTO {

    private final Set<String> seats;

    private final int amount;

    private final boolean open;

    private final Set<String> winners;

    public PotDTO(Set<String> seats, int amount, boolean open,
            Set<String> winners) {
        this.seats = seats;
        this.amount = amount;
        this.open = open;
        this.winners = winners;
    }

    @JsonProperty
    public Set<String> getSeats() {
        return seats;
    }

    @JsonProperty
    public int getAmount() {
        return amount;
    }

    @JsonProperty
    public boolean isOpen() {
        return open;
    }

    @JsonProperty
    public Set<String> getWinners() {
        return winners;
    }

}
