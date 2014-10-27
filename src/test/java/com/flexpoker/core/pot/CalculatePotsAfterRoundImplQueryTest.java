package com.flexpoker.core.pot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.model.Game;
import com.flexpoker.model.Hand;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.test.util.datageneration.DeckGenerator;
import com.flexpoker.test.util.datageneration.GameGenerator;

public class CalculatePotsAfterRoundImplQueryTest {

    private CalculatePotsAfterRoundImplQuery query;

    @Before
    public void setup() {
        query = new CalculatePotsAfterRoundImplQuery();
    }

    @Test
    public void testCalculatePotsAfterRound() {
        Game game = GameGenerator.createGame(9, 9);
        Table table = new Table();

        testCalculatePotsAfterRound1(game, table);
        testCalculatePotsAfterRound2(game, table);
        testCalculatePotsAfterRound3(game, table);
        testCalculatePotsAfterRound4(game, table);
    }

    private void testCalculatePotsAfterRound1(Game game, Table table) {
        Seat seat1 = new Seat(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);

        table.addSeat(seat1);

        table.setCurrentHand(new Hand(new ArrayList<Seat>(), DeckGenerator.createDeck()));

        Set<Pot> pots = query.execute(table);
        Pot pot = ((Pot) pots.toArray()[0]);

        assertEquals(1, pots.size());
        assertEquals(30, pot.getAmount());
        assertTrue(pot.getSeats().contains(seat1));
    }

    private void testCalculatePotsAfterRound2(Game game, Table table) {
        Seat seat1 = new Seat(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);

        table.getSeats().clear();

        table.addSeat(seat1);
        table.addSeat(seat2);

        Set<Pot> pots = query.execute(table);
        Pot pot = ((Pot) pots.toArray()[0]);

        assertEquals(1, pots.size());
        assertEquals(60, pot.getAmount());
        assertTrue(pot.isOpen());
        assertTrue(pot.getSeats().contains(seat1));
        assertTrue(pot.getSeats().contains(seat2));
    }

    private void testCalculatePotsAfterRound3(Game game, Table table) {
        Seat seat1 = new Seat(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        seat2.setAllIn(true);

        table.getSeats().clear();

        table.addSeat(seat1);
        table.addSeat(seat2);

        Set<Pot> pots = query.execute(table);
        Pot pot = ((Pot) pots.toArray()[0]);

        assertEquals(1, pots.size());
        assertEquals(60, pot.getAmount());
        assertFalse(pot.isOpen());
        assertTrue(pot.getSeats().contains(seat1));
        assertTrue(pot.getSeats().contains(seat2));
    }

    private void testCalculatePotsAfterRound4(Game game, Table table) {
        Seat seat1 = new Seat(0);
        seat1.setChipsInFront(30);
        seat1.setStillInHand(true);
        Seat seat2 = new Seat(1);
        seat2.setChipsInFront(30);
        seat2.setStillInHand(true);
        Seat seat3 = new Seat(2);
        seat3.setChipsInFront(30);
        seat3.setStillInHand(true);
        Seat seat4 = new Seat(3);
        seat4.setChipsInFront(30);
        seat4.setStillInHand(true);

        table.getSeats().clear();

        table.addSeat(seat1);
        table.addSeat(seat2);
        table.addSeat(seat3);
        table.addSeat(seat4);

        // simulate preflop
        Set<Pot> pots = query.execute(table);
        table.getCurrentHand().setPots(pots);
        Pot pot = ((Pot) pots.toArray()[0]);

        assertEquals(1, pots.size());
        assertEquals(120, pot.getAmount());
        assertTrue(pot.isOpen());
        assertTrue(pot.getSeats().contains(seat1));
        assertTrue(pot.getSeats().contains(seat2));
        assertTrue(pot.getSeats().contains(seat3));
        assertTrue(pot.getSeats().contains(seat4));

        seat1.setChipsInFront(50);
        seat2.setChipsInFront(50);
        seat3.setChipsInFront(50);
        seat4.setChipsInFront(50);

        // simulate preturn
        pots = query.execute(table);
        table.getCurrentHand().setPots(pots);
        pot = ((Pot) pots.toArray()[0]);

        assertEquals(1, pots.size());
        assertEquals(320, pot.getAmount());
        assertTrue(pot.isOpen());
        assertTrue(pot.getSeats().contains(seat1));
        assertTrue(pot.getSeats().contains(seat2));
        assertTrue(pot.getSeats().contains(seat3));
        assertTrue(pot.getSeats().contains(seat4));

        seat1.setChipsInFront(20);
        seat1.setAllIn(true);
        seat2.setChipsInFront(40);
        seat2.setAllIn(true);
        seat3.setChipsInFront(90);
        seat4.setChipsInFront(90);

        // simulate preriver
        pots = query.execute(table);
        table.getCurrentHand().setPots(pots);

        for (Pot loopPot : pots) {
            switch (loopPot.getAmount()) {
            case 400:
                assertEquals(400, loopPot.getAmount());
                assertFalse(loopPot.isOpen());
                assertTrue(loopPot.getSeats().contains(seat1));
                assertTrue(loopPot.getSeats().contains(seat2));
                assertTrue(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            case 60:
                assertEquals(60, loopPot.getAmount());
                assertFalse(loopPot.isOpen());
                assertFalse(loopPot.getSeats().contains(seat1));
                assertTrue(loopPot.getSeats().contains(seat2));
                assertTrue(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            case 100:
                assertEquals(100, loopPot.getAmount());
                assertTrue(loopPot.isOpen());
                assertFalse(loopPot.getSeats().contains(seat1));
                assertFalse(loopPot.getSeats().contains(seat2));
                assertTrue(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            }
        }

        assertEquals(3, pots.size());

        seat3.setChipsInFront(100);
        seat3.setAllIn(true);
        seat4.setChipsInFront(350);

        // simulate last round
        pots = query.execute(table);
        table.getCurrentHand().setPots(pots);

        for (Pot loopPot : pots) {
            switch (loopPot.getAmount()) {
            case 400:
                assertEquals(400, loopPot.getAmount());
                assertFalse(loopPot.isOpen());
                assertTrue(loopPot.getSeats().contains(seat1));
                assertTrue(loopPot.getSeats().contains(seat2));
                assertTrue(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            case 60:
                assertEquals(60, loopPot.getAmount());
                assertFalse(loopPot.isOpen());
                assertFalse(loopPot.getSeats().contains(seat1));
                assertTrue(loopPot.getSeats().contains(seat2));
                assertTrue(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            case 300:
                assertEquals(300, loopPot.getAmount());
                assertFalse(loopPot.isOpen());
                assertFalse(loopPot.getSeats().contains(seat1));
                assertFalse(loopPot.getSeats().contains(seat2));
                assertTrue(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            case 250:
                assertEquals(250, loopPot.getAmount());
                assertTrue(loopPot.isOpen());
                assertFalse(loopPot.getSeats().contains(seat1));
                assertFalse(loopPot.getSeats().contains(seat2));
                assertFalse(loopPot.getSeats().contains(seat3));
                assertTrue(loopPot.getSeats().contains(seat4));
                break;
            }
        }

        assertEquals(4, pots.size());
    }

}
