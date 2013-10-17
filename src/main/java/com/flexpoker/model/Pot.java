package com.flexpoker.model;

import java.util.HashSet;
import java.util.Set;

public class Pot {

    private final Set<Seat> seats;

    private int amount;

    private boolean open;

    private final Set<Seat> winners;
    
    public Pot() {
        winners = new HashSet<>();
        seats = new HashSet<>();
        open = true;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public int getAmount() {
        return amount;
    }

    public void addChips(int chips) {
        this.amount += chips;
    }

    public boolean isOpen() {
        return open;
    }

    public void closePot() {
        open = false;
    }

    public Set<Seat> getWinners() {
        return winners;
    }
    
    public void addWinner(Seat seat) {
        winners.add(seat);
    }

}
