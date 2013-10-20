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
 *
 * @author cwoolner
 */
public class UserGameStatus {

    private User user;

    private Integer chips;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getChips() {
        return chips;
    }

    public void setChips(Integer chips) {
        this.chips = chips;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
