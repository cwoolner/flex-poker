package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.flexpoker.model.Game;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Table;
import com.flexpoker.util.Context;

public class PotBsoImplTest {

    private PotBsoImpl bso = (PotBsoImpl) Context.instance()
            .getBean("potBso");

    @Test
    public void testCalculatePotsAfterRound() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateNewHandPot() {
        Game game = new Game();
        game.setId(1);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        bso.createNewHandPot(game, table1);
        bso.createNewHandPot(game, table2);

        List<Pot> pots = bso.fetchAllPots(game, table1);
        assertTrue(pots.isEmpty());
        pots = bso.fetchAllPots(game, table2);
        assertTrue(pots.isEmpty());
    }

    @Test
    public void testFetchAllPots() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveGame() {
        Game game = new Game();
        game.setId(1);
        Table table = new Table();
        table.setId(1);
        bso.createNewHandPot(game, table);
        bso.fetchAllPots(game, table);

        bso.removeGame(game);
        try {
            bso.fetchAllPots(game, table);
            fail("Should have thrown NPE.");
        } catch (NullPointerException e) {}
    }

    @Test
    public void testRemoveSeatFromPots() {
        fail("Not yet implemented");
    }

    @Test
    public void testRemoveTable() {
        Game game = new Game();
        game.setId(1);
        Table table = new Table();
        table.setId(1);
        bso.createNewHandPot(game, table);
        bso.fetchAllPots(game, table);

        bso.removeTable(game, table);
        assertNull(bso.fetchAllPots(game, table));
    }

    @Test
    public void testSetWinners() {
        fail("Not yet implemented");
    }

}
