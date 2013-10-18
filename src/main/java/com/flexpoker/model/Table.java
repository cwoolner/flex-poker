package com.flexpoker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;

import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.HasUserGameStatusPredicate;
import com.flexpoker.util.SmallBlindSeatPredicate;
import com.flexpoker.util.StillInHandSeatPredicate;

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
    
    public int getNumberOfPlayers() {
        int numberOfUsers = 0;

        for (Seat seat : seats) {
            if (seat.getUserGameStatus() != null) {
                numberOfUsers++;
            }
        }

        return numberOfUsers;
    }
    
    public Hand getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(Hand currentHand) {
        this.currentHand = currentHand;
    }
    
    public Seat getActionOnSeat() {
        return (Seat) CollectionUtils.find(seats, new ActionOnSeatPredicate());
    }
    
    public Seat getButtonSeat() {
        return (Seat) CollectionUtils.find(seats, new ButtonSeatPredicate());
    }
    
    public Seat getSmallBlindSeat() {
        return (Seat) CollectionUtils.find(seats, new SmallBlindSeatPredicate());
    }
    
    public Seat getBigBlindSeat() {
        return (Seat) CollectionUtils.find(seats, new BigBlindSeatPredicate());
    }
    
    public int getNumberOfPlayersStillInHand() {
        return CollectionUtils.countMatches(seats, new StillInHandSeatPredicate());
    }
    
    public int getNumberOfPlayersAtTable() {
        return CollectionUtils.countMatches(seats, new HasUserGameStatusPredicate());
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Table other = (Table) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
