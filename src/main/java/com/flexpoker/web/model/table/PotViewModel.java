package com.flexpoker.web.model.table;

import java.util.Set;

public class PotViewModel {

    private Set<String> seats;

    private int amount;

    private boolean open;

    private Set<String> winners;
    
    public PotViewModel(Set<String> seats, int amount, boolean open, Set<String> winners) {
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
