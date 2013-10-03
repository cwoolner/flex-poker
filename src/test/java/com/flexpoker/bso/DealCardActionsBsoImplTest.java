package com.flexpoker.bso;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.test.util.datageneration.GameGenerator;

public class DealCardActionsBsoImplTest {

    private DealCardActionsBsoImpl bso;
    
    private DeckBso mockDeckBso;
    
    private UUID[] uuidArray;
    
    @Before
    public void setup() {
        mockDeckBso = mock(DeckBso.class);
        bso = new DealCardActionsBsoImpl(mockDeckBso);
        uuidArray = new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
    }
    
    @Test
    public void testFetchPocketCards() {
        Game game = new GameGenerator().get(uuidArray);
        when(mockDeckBso.fetchPocketCards(any(User.class), eq(game), any(Table.class))).thenReturn(new PocketCards());

        Table table;

        try {
            table = game.getTables().get(0);
            bso.fetchPocketCards(new User(), game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table = game.getTables().get(1);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table = game.getTables().get(2);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table = game.getTables().get(3);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table = game.getTables().get(4);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table = game.getTables().get(5);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

    }

    @Test
    public void testFetchFlopCards() {
        Game game = new GameGenerator().get(uuidArray);
        when(mockDeckBso.fetchFlopCards(eq(game), any(Table.class))).thenReturn(new FlopCards());

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
        when(mockDeckBso.fetchRiverCard(eq(game), any(Table.class))).thenReturn(new RiverCard());

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
        when(mockDeckBso.fetchTurnCard(eq(game), any(Table.class))).thenReturn(new TurnCard());

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
