package com.flexpoker.core.tablebalancer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.test.util.datageneration.GameGenerator;
import com.flexpoker.util.DataUtilsForTests;

public class TestAreTablesBalancedImplQuery {

    @Mock private GameRepository mockGameRepository;
    
    private AreTablesBalancedImplQuery command;
    
    private Game game;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new AreTablesBalancedImplQuery(mockGameRepository);
    }
    
    @Test
    public void testSimpleSingleTable() {
        game = GameGenerator.createGame(9, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        game.addTable(table1);
        assertTrue(command.execute(game.getId()));
    }
    
    @Test
    public void testImbalanceByTwoUnderMaxThreshold() {
        game = GameGenerator.createGame(9, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        game.addTable(table1);
        game.addTable(table2);
        assertFalse(command.execute(game.getId()));
    }
    
    
    @Test
    public void testBalancedByOneUnderMaxThreshold() {
        game = GameGenerator.createGame(18, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        game.addTable(table1);
        game.addTable(table2);
        assertTrue(command.execute(game.getId()));
    }
    
    @Test
    public void testShouldOnlyBeTwoTablesNotThree() {
        game = GameGenerator.createGame(18, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 4, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 4, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(game.getId()));
    }
    
    @Test
    public void testThreeTablesTwoOutOfBalance() {
        game = GameGenerator.createGame(27, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 9, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 8, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(game.getId()));
    }
    
    @Test
    public void testThreeTablesPerfectlyInBalance() {
        game = GameGenerator.createGame(27, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 7, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertTrue(command.execute(game.getId()));
    }
    
    @Test
    public void testShouldBeExactlyTwoTablesNotThree() {
        game = GameGenerator.createGame(27, 9);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(game.getId()));
    }
    
    @Test
    public void testShouldBeExactlyThreeTables() {
        game = GameGenerator.createGame(27, 6);
        when(mockGameRepository.findById(game.getId())).thenReturn(game);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        DataUtilsForTests.fillTableWithUsers(table1, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 6);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertTrue(command.execute(game.getId()));
    }
}
