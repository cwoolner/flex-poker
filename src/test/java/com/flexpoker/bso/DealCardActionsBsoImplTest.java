package com.flexpoker.bso;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.RealTimeGameBso;
import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.test.util.datageneration.RealTimeGameGenerator;


public class DealCardActionsBsoImplTest {

    private DealCardActionsBsoImpl bso;
    
    private DeckBso mockDeckBso;
    
    private RealTimeGameBso mockRealTimeGameBso;
    
    private ValidationBso mockValidationBso;
    
    @Before
    public void setup() {
        mockDeckBso = mock(DeckBso.class);
        mockRealTimeGameBso = mock(RealTimeGameBso.class);
        mockValidationBso = mock(ValidationBso.class);
        bso = new DealCardActionsBsoImpl(mockDeckBso, mockRealTimeGameBso, mockValidationBso);
    }
    
    @Test
    public void testFetchPocketCards() {
        RealTimeGame generatedRealTimeGame = new RealTimeGameGenerator().get(new Game());
        when(mockRealTimeGameBso.get(any(Game.class))).thenReturn(generatedRealTimeGame);
        when(mockDeckBso.fetchPocketCards(any(User.class), any(Game.class), any(Table.class))).thenReturn(new PocketCards());

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
        RealTimeGame generatedRealTimeGame = new RealTimeGameGenerator().get(new Game());
        when(mockRealTimeGameBso.get(any(Game.class))).thenReturn(generatedRealTimeGame);
        when(mockDeckBso.fetchFlopCards(any(Game.class), any(Table.class))).thenReturn(new FlopCards());

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
        RealTimeGame generatedRealTimeGame = new RealTimeGameGenerator().get(new Game());
        when(mockRealTimeGameBso.get(any(Game.class))).thenReturn(generatedRealTimeGame);
        when(mockDeckBso.fetchRiverCard(any(Game.class), any(Table.class))).thenReturn(new RiverCard());

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
        RealTimeGame generatedRealTimeGame = new RealTimeGameGenerator().get(new Game());
        when(mockRealTimeGameBso.get(any(Game.class))).thenReturn(generatedRealTimeGame);
        when(mockDeckBso.fetchTurnCard(any(Game.class), any(Table.class))).thenReturn(new TurnCard());

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
