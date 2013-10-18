package com.flexpoker.model;

import com.flexpoker.model.card.PocketCards;

public class Seat implements Comparable<Seat> {

    private UserGameStatus userGameStatus;

    private final int position;

    private boolean stillInHand;

    private boolean allIn;

    private boolean playerJustLeft;

    private int chipsInFront;

    private int raiseTo;

    private int callAmount;

    private boolean button;

    private boolean smallBlind;

    private boolean bigBlind;

    private boolean actionOn;

    private PocketCards showCards;
    
    public Seat(int position) {
        this.position = position;
    }

    public UserGameStatus getUserGameStatus() {
        return userGameStatus;
    }

    public void setUserGameStatus(UserGameStatus userGameStatus) {
        this.userGameStatus = userGameStatus;
    }

    public Integer getPosition() {
        return position;
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

    public int getRaiseTo() {
        return raiseTo;
    }

    public void setRaiseTo(int raiseTo) {
        this.raiseTo = raiseTo;
    }

    public int getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(int callAmount) {
        this.callAmount = callAmount;
    }

    public boolean isButton() {
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }

    public boolean isSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(boolean smallBlind) {
        this.smallBlind = smallBlind;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(boolean bigBlind) {
        this.bigBlind = bigBlind;
    }

    public boolean isActionOn() {
        return actionOn;
    }

    public void setActionOn(boolean actionOn) {
        this.actionOn = actionOn;
    }

    public PocketCards getShowCards() {
        return showCards;
    }

    public void setShowCards(PocketCards showCards) {
        this.showCards = showCards;
    }

    @Override
    public int compareTo(Seat seat) {
        return Integer.compare(position, seat.getPosition());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + position;
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
        if (position != other.position)
            return false;
        return true;
    }

    
}
