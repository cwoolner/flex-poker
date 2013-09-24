package com.flexpoker.bso;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.ValidationBso;
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
    
    private ValidationBso mockValidationBso;
    
    private UUID[] uuidArray;
    
    @Before
    public void setup() {
        mockDeckBso = mock(DeckBso.class);
        mockValidationBso = mock(ValidationBso.class);
        bso = new DealCardActionsBsoImpl(mockDeckBso, mockValidationBso);
        uuidArray = new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
    }
    
    @Test
    public void testFetchPocketCards() {
        Game game = new GameGenerator().get(new Game(), uuidArray);
        when(mockDeckBso.fetchPocketCards(any(User.class), any(Game.class), any(Table.class))).thenReturn(new PocketCards());

        Table table = new Table();

        try {
            table.setId(uuidArray[0]);
            bso.fetchPocketCards(new User(), game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(uuidArray[1]);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table.setId(uuidArray[2]);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table.setId(uuidArray[3]);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table.setId(uuidArray[4]);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

        table.setId(uuidArray[5]);
        assertNotNull(bso.fetchPocketCards(new User(), game, table));

    }

    @Test
    public void testFetchFlopCards() {
        Game game = new GameGenerator().get(new Game(), uuidArray);
        when(mockDeckBso.fetchFlopCards(any(Game.class), any(Table.class))).thenReturn(new FlopCards());

        Table table = new Table();

        try {
            table.setId(uuidArray[0]);
            bso.fetchFlopCards(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(uuidArray[1]);
            bso.fetchFlopCards(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(uuidArray[2]);
        assertNotNull(bso.fetchFlopCards(game, table));

        table.setId(uuidArray[3]);
        assertNotNull(bso.fetchFlopCards(game, table));

        table.setId(uuidArray[4]);
        assertNotNull(bso.fetchFlopCards(game, table));

        table.setId(uuidArray[5]);
        assertNotNull(bso.fetchFlopCards(game, table));
    }

    @Test
    public void testFetchRiverCard() {
        Game game = new GameGenerator().get(new Game(), uuidArray);
        when(mockDeckBso.fetchRiverCard(any(Game.class), any(Table.class))).thenReturn(new RiverCard());

        Table table = new Table();

        try {
            table.setId(uuidArray[0]);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(uuidArray[1]);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(uuidArray[2]);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(uuidArray[3]);
            bso.fetchRiverCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(uuidArray[4]);
        assertNotNull(bso.fetchRiverCard(game, table));

        table.setId(uuidArray[5]);
        assertNotNull(bso.fetchRiverCard(game, table));
    }

    @Test
    public void testFetchTurnCard() {
        Game game = new GameGenerator().get(new Game(), uuidArray);
        when(mockDeckBso.fetchTurnCard(any(Game.class), any(Table.class))).thenReturn(new TurnCard());

        Table table = new Table();

        try {
            table.setId(uuidArray[0]);
            bso.fetchTurnCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(uuidArray[1]);
            bso.fetchTurnCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        try {
            table.setId(uuidArray[2]);
            bso.fetchTurnCard(game, table);
            fail("Should have thrown a FlexPokerException.");
        } catch (FlexPokerException e) {}

        table.setId(uuidArray[3]);
        bso.fetchTurnCard(game, table);

        table.setId(uuidArray[4]);
        bso.fetchTurnCard(game, table);

        table.setId(uuidArray[5]);
        bso.fetchTurnCard(game, table);

    }

}
