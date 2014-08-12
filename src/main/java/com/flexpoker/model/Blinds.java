package com.flexpoker.model;

public class Blinds {

    private final int smallBlind;

    private final int bigBlind;

    public Blinds(final int smallBlind, final int bigBlind) {
        if (smallBlind > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException("Small blind can't be that large.");
        }
        if (smallBlind < 1) {
            throw new IllegalArgumentException("Small blind must be greater than 0.");
        }
        if (bigBlind < 1) {
            throw new IllegalArgumentException("Big blind must be greater than 0.");
        }
        if (bigBlind != smallBlind * 2) {
            throw new IllegalArgumentException("The big blind must be twice as "
                    + "large as the small blind.");
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
