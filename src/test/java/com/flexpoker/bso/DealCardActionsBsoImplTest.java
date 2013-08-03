package com.flexpoker.bso;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;


public class DealCardActionsBsoImplTest {

    private DealCardActionsBsoImpl bso = new DealCardActionsBsoImpl();            

    @Test
    public void testFetchPocketCards() {
        Table table = new Table();

        try {
            table.setId(1);
            bso.fetchPocketCards(new User(), new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(2);
        assertNotNull(bso.fetchPocketCards(new User(), new Game(), table));

        table.setId(3);
        assertNotNull(bso.fetchPocketCards(new User(), new Game(), table));

        table.setId(4);
        assertNotNull(bso.fetchPocketCards(new User(), new Game(), table));

        table.setId(5);
        assertNotNull(bso.fetchPocketCards(new User(), new Game(), table));

        table.setId(6);
        assertNotNull(bso.fetchPocketCards(new User(), new Game(), table));

    }

    @Test
    public void testFetchFlopCards() {
        Table table = new Table();

        try {
            table.setId(1);
            bso.fetchFlopCards(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(2);
            bso.fetchFlopCards(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(3);
        assertNotNull(bso.fetchFlopCards(new Game(), table));

        table.setId(4);
        assertNotNull(bso.fetchFlopCards(new Game(), table));

        table.setId(5);
        assertNotNull(bso.fetchFlopCards(new Game(), table));

        table.setId(6);
        assertNotNull(bso.fetchFlopCards(new Game(), table));

    }

    @Test
    public void testFetchRiverCard() {
        Table table = new Table();

        try {
            table.setId(1);
            bso.fetchRiverCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(2);
            bso.fetchRiverCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(3);
            bso.fetchRiverCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(4);
            bso.fetchRiverCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(5);
        assertNotNull(bso.fetchRiverCard(new Game(), table));

        table.setId(6);
        assertNotNull(bso.fetchRiverCard(new Game(), table));
    }

    @Test
    public void testFetchTurnCard() {
        Table table = new Table();

        try {
            table.setId(1);
            bso.fetchTurnCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(2);
            bso.fetchTurnCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(3);
            bso.fetchTurnCard(new Game(), table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(4);
        bso.fetchTurnCard(new Game(), table);

        table.setId(5);
        bso.fetchTurnCard(new Game(), table);

        table.setId(6);
        bso.fetchTurnCard(new Game(), table);

    }

}
