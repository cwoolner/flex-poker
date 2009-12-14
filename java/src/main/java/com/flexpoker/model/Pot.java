package com.flexpoker.model;

import java.util.List;

public class Pot {

    private List<Seat> seats;

    private int amount;

    private boolean open;

    private List<Seat> winners;

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<Seat> getWinners() {
        return winners;
    }

    public void setWinners(List<Seat> winners) {
        this.winners = winners;
    }

}
