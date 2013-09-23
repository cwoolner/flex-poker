package com.flexpoker.model;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;

import com.flexpoker.util.Constants;

public class Table {

    private UUID id;

    private List<Seat> seats;

    private int totalPotAmount;

    private List<Integer> potAmounts;
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getTotalPotAmount() {
        return totalPotAmount;
    }

    public void setTotalPotAmount(int totalPotAmount) {
        this.totalPotAmount = totalPotAmount;
    }

    public List<Integer> getPotAmounts() {
        return potAmounts;
    }

    public void setPotAmounts(List<Integer> potAmounts) {
        this.potAmounts = potAmounts;
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
    
    /**
     * Verify that the table is in a valid state before performing operations
     * on it.
     */
    public void validateTable() {
        if (CollectionUtils.isEmpty(seats)) {
            throw new IllegalArgumentException("seats cannot be empty.");
        }
        if (seats.size() > Constants.MAX_PLAYERS_PER_TABLE
                || seats.size() < Constants.MIN_PLAYERS_PER_TABLE) {
            throw new IllegalArgumentException("Number of seats must be "
                    + "between: " + Constants.MIN_PLAYERS_PER_TABLE
                    + " and " + Constants.MAX_PLAYERS_PER_TABLE);
        }
        int numberOfUsers = getNumberOfPlayers();
        if (numberOfUsers > Constants.MAX_PLAYERS_PER_TABLE
                || numberOfUsers < Constants.MIN_PLAYERS_PER_TABLE) {
            throw new IllegalArgumentException("Number of users must be "
                    + "between: " + Constants.MIN_PLAYERS_PER_TABLE
                    + " and " + Constants.MAX_PLAYERS_PER_TABLE);
        }
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
