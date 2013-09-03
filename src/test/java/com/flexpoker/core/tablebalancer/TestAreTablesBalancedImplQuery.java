package com.flexpoker.core.tablebalancer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.model.Game;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Table;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.DataUtilsForTests;

public class TestAreTablesBalancedImplQuery {

    @Mock private RealTimeGameRepository mockRealTimeGameRepository;
    
    @Mock private GameRepository mockGameRepository;
    
    private AreTablesBalancedImplQuery command;
    
    private Integer gameId;
    
    private RealTimeGame realTimeGame;
    
    private Game game;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameId = 1;
        realTimeGame = new RealTimeGame();
        game = new Game();
        when(mockRealTimeGameRepository.get(gameId)).thenReturn(realTimeGame);
        when(mockGameRepository.findById(gameId)).thenReturn(game);
        command = new AreTablesBalancedImplQuery(mockRealTimeGameRepository, mockGameRepository);
    }
    
    @Test
    public void testSimpleSingleTable() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        realTimeGame.addTable(table1);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testImbalanceByTwoUnderMaxThreshold() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        assertFalse(command.execute(gameId));
    }
    
    
    @Test
    public void testBalancedByOneUnderMaxThreshold() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testShouldOnlyBeTwoTablesNotThree() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 4, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 4, 9);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testThreeTablesTwoOutOfBalance() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 9, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 8, 9);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testThreeTablesPerfectlyInBalance() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 7, 9);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testShouldBeExactlyTwoTablesNotThree() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 9);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testShouldBeExactlyThreeTables() {
        game.setMaxPlayersPerTable(6);
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 6);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertTrue(command.execute(gameId));
    }
}
