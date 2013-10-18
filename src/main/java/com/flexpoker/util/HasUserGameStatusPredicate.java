package com.flexpoker.util;

import org.apache.commons.collections.Predicate;

import com.flexpoker.model.Seat;

public class HasUserGameStatusPredicate implements Predicate {

    @Override
    public boolean evaluate(Object arg0) {
        return ((Seat) arg0).getUserGameStatus() != null;
    }

}
