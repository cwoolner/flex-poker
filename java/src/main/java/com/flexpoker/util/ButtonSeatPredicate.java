package com.flexpoker.util;

import org.apache.commons.collections.Predicate;

import com.flexpoker.model.Seat;

public class ButtonSeatPredicate implements Predicate {

    @Override
    public boolean evaluate(Object seat) {
        return ((Seat) seat).isButton();
    }

}
