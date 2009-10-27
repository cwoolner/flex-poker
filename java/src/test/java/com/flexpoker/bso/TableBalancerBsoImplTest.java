package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.Context;
import com.flexpoker.util.TestDataUtils;

public class TableBalancerBsoImplTest {

    private TableBalancerBso bso = (TableBalancerBso) Context.instance().getBean("tableBalancerBso");
    
    @Test
    public void testAreTablesBalanced() {
        Table table1 = new Table();
        table1.setId(1);
        TestDataUtils.fillTableWithUsers(table1, 3);
        List<Table> tables = new ArrayList<Table>();
        tables.add(table1);
        assertTrue(bso.areTablesBalanced(tables));

        table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        TestDataUtils.fillTableWithUsers(table1, 3);
        TestDataUtils.fillTableWithUsers(table2, 5);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        assertFalse(bso.areTablesBalanced(tables));

        table1 = new Table();
        table1.setId(1);
        table2 = new Table();
        table2.setId(2);
        TestDataUtils.fillTableWithUsers(table1, 7);
        TestDataUtils.fillTableWithUsers(table2, 6);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        assertTrue(bso.areTablesBalanced(tables));

        // imbalance because there should only be two tables, not three
        table1 = new Table();
        table1.setId(1);
        table2 = new Table();
        table2.setId(2);
        Table table3 = new Table();
        table3.setId(3);
        TestDataUtils.fillTableWithUsers(table1, 4);
        TestDataUtils.fillTableWithUsers(table2, 5);
        TestDataUtils.fillTableWithUsers(table3, 4);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        assertFalse(bso.areTablesBalanced(tables));

        // imbalance because 7 and 9 are too great of a difference
        table1 = new Table();
        table1.setId(1);
        table2 = new Table();
        table2.setId(2);
        table3 = new Table();
        table3.setId(3);
        TestDataUtils.fillTableWithUsers(table1, 7);
        TestDataUtils.fillTableWithUsers(table2, 9);
        TestDataUtils.fillTableWithUsers(table3, 8);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        assertFalse(bso.areTablesBalanced(tables));

        table1 = new Table();
        table1.setId(1);
        table2 = new Table();
        table2.setId(2);
        table3 = new Table();
        table3.setId(3);
        TestDataUtils.fillTableWithUsers(table1, 7);
        TestDataUtils.fillTableWithUsers(table2, 7);
        TestDataUtils.fillTableWithUsers(table3, 7);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        assertTrue(bso.areTablesBalanced(tables));

        // imbalance because there should only be two tables, not three
        table1 = new Table();
        table1.setId(1);
        table2 = new Table();
        table2.setId(2);
        table3 = new Table();
        table3.setId(3);
        TestDataUtils.fillTableWithUsers(table1, 6);
        TestDataUtils.fillTableWithUsers(table2, 6);
        TestDataUtils.fillTableWithUsers(table3, 6);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        assertFalse(bso.areTablesBalanced(tables));
    }

    @Test
    public void testAssignInitialTablesForNewGame() {
        Set<UserGameStatus> userGameStatuses = null;
        try {
            bso.assignInitialTablesForNewGame(userGameStatuses, 9);
            fail("An exception should have been thrown.  Can't send in an empty Set.");
        } catch (IllegalArgumentException e) {}

        userGameStatuses = createUserGameStatusSet(0);
        try {
            bso.assignInitialTablesForNewGame(userGameStatuses, 9);
            fail("An exception should have been thrown.  Can't send in an empty Set.");
        } catch (IllegalArgumentException e) {}

        userGameStatuses = createUserGameStatusSet(7);
        try {
            assertEquals(1, bso.assignInitialTablesForNewGame(userGameStatuses, 10).size());
            fail("An exception should have been thrown.  10 is too large of a table.");
        } catch (IllegalArgumentException e) {}
        try {
            assertEquals(1, bso.assignInitialTablesForNewGame(userGameStatuses, 1).size());
            fail("An exception should have been thrown.  1 is too small of a table.");
        } catch (IllegalArgumentException e) {}
        try {
            assertEquals(1, bso.assignInitialTablesForNewGame(userGameStatuses, 2).size());
            fail("A heads-up tournament must start with an even number of people.");
        } catch (IllegalArgumentException e) {}

        userGameStatuses = createUserGameStatusSet(4);
        List<Table> tables = bso.assignInitialTablesForNewGame(userGameStatuses, 9);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 9, 4);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 6);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 6, 4);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 4);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 4, 4);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 3);
        assertEquals(2, tables.size());
        verifyEqualDistribution(tables, 3, 2, 2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 2);
        assertEquals(2, tables.size());
        verifyEqualDistribution(tables, 2, 2, 2);

        userGameStatuses = TestDataUtils.createUserGameStatusSet(20);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 9);
        assertEquals(3, tables.size());
        verifyEqualDistribution(tables, 9, 7, 7, 6);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 6);
        assertEquals(4, tables.size());
        verifyEqualDistribution(tables, 6, 5, 5, 5, 5);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 4);
        assertEquals(5, tables.size());
        verifyEqualDistribution(tables, 4, 4, 4, 4, 4, 4);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 3);
        assertEquals(7, tables.size());
        verifyEqualDistribution(tables, 3, 3, 3, 3, 3, 3, 3, 2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 2);
        assertEquals(10, tables.size());
        verifyEqualDistribution(tables, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);

        userGameStatuses = TestDataUtils.createUserGameStatusSet(2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 9);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 9, 2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 6);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 6, 2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 4);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 4, 2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 3);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 3, 2);
        tables = bso.assignInitialTablesForNewGame(userGameStatuses, 2);
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 2, 2);
    }

    private void verifyEqualDistribution(List<Table> tables,
            int maxPlayersPerTableForGame, int... expectedUserGameStatusesPerTable) {

        for (Table table : tables) {
            assertEquals(maxPlayersPerTableForGame, table.getSeats().size());
        }

        int[] actualUserGameStatusesPerTable = new int[tables.size()];

        int i = 0;
        for (Table table : tables) {
            int numberOfUserGameStatuses = 0;
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null) {
                    numberOfUserGameStatuses++;
                }
            }
            actualUserGameStatusesPerTable[i] = numberOfUserGameStatuses;
            i++;
        }

        Arrays.sort(expectedUserGameStatusesPerTable);
        Arrays.sort(actualUserGameStatusesPerTable);

        assertArrayEquals(expectedUserGameStatusesPerTable, actualUserGameStatusesPerTable);
    }

}