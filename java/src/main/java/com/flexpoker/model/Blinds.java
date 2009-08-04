package com.flexpoker.model;

public class Blinds {

    private int smallBlind;

    private int bigBlind;

    public Blinds(int smallBlind, int bigBlind) {
        if (bigBlind != smallBlind * 2) {
            throw new IllegalArgumentException("The big blind must be twice "
                + "as large as the small blind.");
        }

        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

}
