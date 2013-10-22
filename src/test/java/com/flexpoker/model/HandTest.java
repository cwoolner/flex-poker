package com.flexpoker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.flexpoker.test.util.datageneration.DeckGenerator;

public class HandTest {

    @Test
    public void testMoveToNextDealerState() {
        Hand hand = new Hand(new ArrayList<Seat>(), DeckGenerator.createDeck());
        assertEquals(HandDealerState.NONE, hand.getHandDealerState());
        
        hand.moveToNextDealerState();
        assertEquals(HandDealerState.POCKET_CARDS_DEALT, hand.getHandDealerState());
        
        hand.moveToNextDealerState();
        assertEquals(HandDealerState.FLOP_DEALT, hand.getHandDealerState());

        hand.moveToNextDealerState();
        assertEquals(HandDealerState.TURN_DEALT, hand.getHandDealerState());

        hand.moveToNextDealerState();
        assertEquals(HandDealerState.RIVER_DEALT, hand.getHandDealerState());
        
        hand.moveToNextDealerState();
        assertEquals(HandDealerState.COMPLETE, hand.getHandDealerState());
        
        hand.moveToNextDealerState();
        assertEquals(HandDealerState.COMPLETE, hand.getHandDealerState());
    }

    @Test
    public void testEmptyUserActions() {
        List<Seat> seats = new ArrayList<Seat>();
        Seat seat1 = new Seat(0);
        Seat seat2 = new Seat(1);

        seats.add(seat1);
        seats.add(seat2);

        Hand realTimeHand = new Hand(seats, DeckGenerator.createDeck());

        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.CHECK, seat1));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.CHECK, seat2));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.CALL, seat1));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.CALL, seat2));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.FOLD, seat1));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.FOLD, seat2));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.RAISE, seat1));
        assertFalse(realTimeHand.isUserAllowedToPerformAction(GameEventType.RAISE, seat2));
    }

}
