package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.test.util.datageneration.GameGenerator;

public class DealCardActionsBsoImplTest {

    private DealCardActionsBsoImpl bso;
    
    private UUID[] uuidArray;
    
    @Before
    public void setup() {
        bso = new DealCardActionsBsoImpl();
        uuidArray = new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
    }
    
    @Test
    public void testFetchFlopCards() {
        Game game = new GameGenerator().get(uuidArray);
        Table table;

        try {
            table = game.getTables().get(0);
            bso.fetchFlopCards(game, table);
            fail("Should have thrown an  FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table = game.getTables().get(1);
            bso.fetchFlopCards(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table = game.getTables().get(2);
        assertNotNull(bso.fetchFlopCards(game, table));

        table = game.getTables().get(3);
        assertNotNull(bso.fetchFlopCards(game, table));

        table = game.getTables().get(4);
        assertNotNull(bso.fetchFlopCards(game, table));

        table = game.getTables().get(5);
        assertNotNull(bso.fetchFlopCards(game, table));
    }

    @Test
    public void testFetchRiverCard() {
        Game game = new GameGenerator().get(uuidArray);
        Table table;

        try {
            table = game.getTables().get(0);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table = game.getTables().get(1);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table = game.getTables().get(2);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table = game.getTables().get(3);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table = game.getTables().get(4);
        assertNotNull(bso.fetchRiverCard(game, table));

        table = game.getTables().get(5);
        assertNotNull(bso.fetchRiverCard(game, table));
    }

    @Test
    public void testFetchTurnCard() {
        Game game = new GameGenerator().get(uuidArray);

        Table table;
        
        try {
            table = game.getTables().get(0);
            bso.fetchTurnCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table = game.getTables().get(1);
            bso.fetchTurnCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table = game.getTables().get(2);
            bso.fetchTurnCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table = game.getTables().get(3);
        bso.fetchTurnCard(game, table);

        table = game.getTables().get(4);
        bso.fetchTurnCard(game, table);

        table = game.getTables().get(5);
        bso.fetchTurnCard(game, table);

    }

}
