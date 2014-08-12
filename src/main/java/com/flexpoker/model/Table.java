package com.flexpoker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Table {

    private final UUID id;

    private final List<Seat> seats;

    private Hand currentHand;
    
    public Table() {
        id = UUID.randomUUID();
        seats = new ArrayList<>();
    }
    
    public UUID getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public void resetRaiseTo() {
        for (Seat seat : seats) {
            seat.setRaiseTo(0);
        }
    }

    public void resetCallAmounts() {
        for (Seat seat : seats) {
            seat.setCallAmount(0);
        }
    }

    public void resetChipsInFront() {
        for (Seat seat : seats) {
            seat.setChipsInFront(0);
        }
    }

    public void resetShowCards() {
        for (Seat seat : seats) {
            seat.setShowCards(null);
        }
    }
    
    public void resetActionOn() {
        for (Seat seat : seats) {
            seat.setActionOn(false);
        }
    }
    
    public void resetStillInHand() {
        for (Seat seat : seats) {
            if (seat.getUserGameStatus() != null) {
                seat.setStillInHand(true);
            }
        }
    }
    
    public Hand getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(Hand currentHand) {
        this.currentHand = currentHand;
    }
    
    public Seat getActionOnSeat() {
        return seats.stream().filter(x -> x.isActionOn()).findAny().orElse(null);
    }

    public Seat getButtonSeat() {
        return seats.stream().filter(x -> x.isButton()).findAny().orElse(null);
    }

    public Seat getSmallBlindSeat() {
        return seats.stream().filter(x -> x.isSmallBlind()).findAny().orElse(null);
    }

    public Seat getBigBlindSeat() {
        return seats.stream().filter(x -> x.isBigBlind()).findAny().orElse(null);
    }

    public int getNumberOfPlayersStillInHand() {
        return (int) seats.stream().filter(x -> x.isStillInHand()).count();
    }

    public int getNumberOfPlayers() {
        return (int) seats.stream().filter(x -> x.getUserGameStatus() != null)
                .count();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Table rhs = (Table) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(id, rhs.id).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
