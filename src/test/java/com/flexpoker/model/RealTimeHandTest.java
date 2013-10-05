package com.flexpoker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.flexpoker.test.util.datageneration.DeckGenerator;

public class RealTimeHandTest {

    @Test
    public void testRealTimeHand() {
        List<Seat> seats = new ArrayList<Seat>();
        Seat seat1 = new Seat();
        Seat seat2 = new Seat();

        seats.add(seat1);
        seats.add(seat2);

        Hand realTimeHand = new Hand(seats, DeckGenerator.createDeck());

        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.CHECK, seat1));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.CHECK, seat2));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.RAISE, seat1));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.RAISE, seat2));
    }

}
