package com.flexpoker.web.dto.outgoing;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PotDTO {

    private final Set<String> seats;

    private final int amount;

    private final boolean open;

    private final Set<String> winners;

    @JsonCreator
    public PotDTO(
            @JsonProperty(value = "seats") Set<String> seats,
            @JsonProperty(value = "amount") int amount,
            @JsonProperty(value = "open") boolean open,
            @JsonProperty(value = "winners") Set<String> winners) {
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
