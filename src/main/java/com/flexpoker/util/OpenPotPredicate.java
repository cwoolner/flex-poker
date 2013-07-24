package com.flexpoker.util;

import org.apache.commons.collections.Predicate;

import com.flexpoker.model.Pot;

public class OpenPotPredicate implements Predicate {

    @Override
    public boolean evaluate(Object pot) {
        return ((Pot) pot).isOpen();
    }

}