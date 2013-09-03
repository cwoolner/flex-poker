package com.flexpoker.core.tablebalancer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Table;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.DataUtilsForTests;

public class TestAreTablesBalancedImplQuery {

    @Mock private RealTimeGameRepository mockRealTimeGameRepository;
    
    private AreTablesBalancedImplQuery command;
    
    private Integer gameId;
    
    private RealTimeGame realTimeGame;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameId = 1;
        realTimeGame = new RealTimeGame();
        when(mockRealTimeGameRepository.get(gameId)).thenReturn(realTimeGame);
        command = new AreTablesBalancedImplQuery(mockRealTimeGameRepository);
    }
    
    @Test
    public void testSimpleSingleTable() {
        Table table1 = new Table();
        table1.setId(1);
        DataUtilsForTests.fillTableWithUsers(table1, 3);
        realTimeGame.addTable(table1);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testImbalanceByTwoUnderMaxThreshold() {
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        DataUtilsForTests.fillTableWithUsers(table1, 3);
        DataUtilsForTests.fillTableWithUsers(table2, 5);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        assertFalse(command.execute(gameId));
    }
    
    
    @Test
    public void testBalancedByOneUnderMaxThreshold() {
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        DataUtilsForTests.fillTableWithUsers(table1, 7);
        DataUtilsForTests.fillTableWithUsers(table2, 6);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testShouldOnlyBeTwoTablesNotThree() {
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 4);
        DataUtilsForTests.fillTableWithUsers(table2, 5);
        DataUtilsForTests.fillTableWithUsers(table3, 4);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testThreeTablesTwoOutOfBalance() {
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 7);
        DataUtilsForTests.fillTableWithUsers(table2, 9);
        DataUtilsForTests.fillTableWithUsers(table3, 8);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
    @Test
    public void testThreeTablesPerfectlyInBalance() {
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 7);
        DataUtilsForTests.fillTableWithUsers(table2, 7);
        DataUtilsForTests.fillTableWithUsers(table3, 7);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertTrue(command.execute(gameId));
    }
    
    @Test
    public void testShouldBeExactlyTwoTablesNotThree() {
        Table table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        DataUtilsForTests.fillTableWithUsers(table1, 6);
        DataUtilsForTests.fillTableWithUsers(table2, 6);
        DataUtilsForTests.fillTableWithUsers(table3, 6);
        realTimeGame.addTable(table1);
        realTimeGame.addTable(table2);
        realTimeGame.addTable(table3);
        assertFalse(command.execute(gameId));
    }
    
}
