package com.flexpoker.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class acts as a thin wrapper for the User object when actually playing
 * in a game.  Since it represents the user's state within a game, only fields
 * that should not be tied to a particular seat should be included in this
 * class.
 *
 * Only the number of chips has fit that criteria so far since chips are taken
 * with the player when they are moved to a different table.
 */
public class UserGameStatus {

    private final User user;

    private int chips;
    
    public UserGameStatus(User user, int chips) {
        this.user = user;
        this.chips = chips;
    }

    public User getUser() {
        return user;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int chips) {
        this.chips += chips;
    }
    
    public void removeChips(int chips) {
        this.chips -= chips;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
