package com.flexpoker.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Pot {

    private final Set<UUID> playersInvolved;

    private final Set<UUID> winners;

    private int amount;

    private boolean open;

    public Pot() {
        playersInvolved = new HashSet<>();
        winners = new HashSet<>();
        open = true;
    }

    public Set<UUID> getSeats() {
        return playersInvolved;
    }

    public void removeSeat(UUID player) {
        playersInvolved.remove(player);
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

    public Set<UUID> getWinners() {
        return winners;
    }

    public void addWinners(Collection<UUID> seats) {
        winners.addAll(seats);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
