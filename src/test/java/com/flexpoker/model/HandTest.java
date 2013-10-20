package com.flexpoker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
    
}
