package com.flexpoker.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Seat implements Comparable<Seat> {

    private UserGameStatus userGameStatus;

    private Integer position;

    private boolean stillInHand;

    private boolean allIn;

    private boolean playerJustLeft;

    private int chipsInFront;

    public UserGameStatus getUserGameStatus() {
        return userGameStatus;
    }

    public void setUserGameStatus(UserGameStatus userGameStatus) {
        this.userGameStatus = userGameStatus;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean isStillInHand() {
        return stillInHand;
    }

    public void setStillInHand(boolean stillInHand) {
        this.stillInHand = stillInHand;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    public boolean isPlayerJustLeft() {
        return playerJustLeft;
    }

    public void setPlayerJustLeft(boolean playerJustLeft) {
        this.playerJustLeft = playerJustLeft;
    }

    public int getChipsInFront() {
        return chipsInFront;
    }

    public void setChipsInFront(int chipsInFront) {
        this.chipsInFront = chipsInFront;
    }

    @Override
    public int compareTo(Seat seat) {
        return position.compareTo(seat.getPosition());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
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
        Seat other = (Seat) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("allIn", allIn)
                .append("position", position)
                .append("stillInHand", stillInHand)
                .append("userGameStatus", userGameStatus)
                .append("playerJustLeft", playerJustLeft)
                .toString();
    }

}
