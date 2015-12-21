package com.flexpoker.game.command.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: This class will eventually be more than just a wrapper for two ints. It
 * will eventually be created with some sort of BlindSchedule object that
 * contains how often blinds raise and the values of the small/big blinds at
 * each level. In addition, this class will eventually be responsible for
 * spitting out events to say "raise the blinds". That's why the class was moved
 * to be more closely associated with the Game domain and is independent from
 * the Table domain.
 */
public class BlindAmounts {

    private final int smallBlind;

    private final int bigBlind;

    @JsonCreator
    public BlindAmounts(@JsonProperty(value = "smallBlind") final int smallBlind,
            @JsonProperty(value = "bigBlind") final int bigBlind) {
        if (smallBlind > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException("Small blind can't be that large.");
        }
        if (smallBlind < 1) {
            throw new IllegalArgumentException("Small blind must be greater than 0.");
        }
        if (bigBlind < 2) {
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
