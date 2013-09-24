package com.flexpoker.core.tablebalancer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.util.DataUtilsForTests;

public class TestAreTablesBalancedImplQuery {

    @Mock private GameRepository mockGameRepository;
    
    private AreTablesBalancedImplQuery command;
    
    private UUID gameId;
    
    private Game game;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameId = UUID.randomUUID();
        game = new Game();
        when(mockGameRepository.findById(gameId)).thenReturn(game);
        command = new AreTablesBalancedImplQuery(mockGameRepository);
    }
    
    @Test
    public void testSimpleSingleTable() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        game.addTable(table1);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testImbalanceByTwoUnderMaxThreshold() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        game.addTable(table1);
        game.addTable(table2);
        assertFalse(command.execute(gameId));
    }
    
    
    @Test
    public void testBalancedByOneUnderMaxThreshold() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        game.addTable(table1);
        game.addTable(table2);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testShouldOnlyBeTwoTablesNotThree() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        Table table3 = new Table();
        table3.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 4, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 4, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testThreeTablesTwoOutOfBalance() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        Table table3 = new Table();
        table3.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 9, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 8, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testThreeTablesPerfectlyInBalance() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        Table table3 = new Table();
        table3.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 7, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 7, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testShouldBeExactlyTwoTablesNotThree() {
        game.setMaxPlayersPerTable(9);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        Table table3 = new Table();
        table3.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 9);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testShouldBeExactlyThreeTables() {
        game.setMaxPlayersPerTable(6);
        Table table1 = new Table();
        table1.setId(UUID.randomUUID());
        Table table2 = new Table();
        table2.setId(UUID.randomUUID());
        Table table3 = new Table();
        table3.setId(UUID.randomUUID());
        DataUtilsForTests.fillTableWithUsers(table1, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table2, 6, 6);
        DataUtilsForTests.fillTableWithUsers(table3, 6, 6);
        game.addTable(table1);
        game.addTable(table2);
        game.addTable(table3);
        assertTrue(command.execute(gameId));
    }
}
