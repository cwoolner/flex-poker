package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import com.flexpoker.model.Table;
import com.flexpoker.model.Seat;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.Context;
import com.flexpoker.util.Constants;

public class TableBalancerBsoImplTest {

    private TableBalancerBso bso = (TableBalancerBso) Context.instance().getBean("tableBalancerBso");
    
    @Test
    public void testAreTablesBalanced() {
        Table table1 = new Table();
        table1.setId(1);
        fillTableWithUsers(table1, 3);
        List<Table> tables = new ArrayList<Table>();
        tables.add(table1);
        assertTrue(bso.areTablesBalanced(tables));

        table1 = new Table();
        table1.setId(1);
        Table table2 = new Table();
        table2.setId(2);
        fillTableWithUsers(table1, 3);
        fillTableWithUsers(table2, 5);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        assertFalse(bso.areTablesBalanced(tables));

        table1 = new Table();
        table1.setId(1);
        table2 = new Table();
        table2.setId(2);
        fillTableWithUsers(table1, 7);
        fillTableWithUsers(table2, 6);
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
        fillTableWithUsers(table1, 4);
        fillTableWithUsers(table2, 5);
        fillTableWithUsers(table3, 4);
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
        fillTableWithUsers(table1, 7);
        fillTableWithUsers(table2, 9);
        fillTableWithUsers(table3, 8);
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
        fillTableWithUsers(table1, 7);
        fillTableWithUsers(table2, 7);
        fillTableWithUsers(table3, 7);
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
        fillTableWithUsers(table1, 6);
        fillTableWithUsers(table2, 6);
        fillTableWithUsers(table3, 6);
        tables = new ArrayList<Table>();
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);
        assertFalse(bso.areTablesBalanced(tables));
    }

    private void fillTableWithUsers(Table table, int numberOfUsers) {
        List<Seat> seats = new ArrayList<Seat>();
        for (int i = 0; i < Constants.MAX_PLAYERS_PER_TABLE; i++) {
            Seat seat = new Seat();
            seat.setPosition(i);
            seats.add(seat);
        }

        for (int i = 0; i < numberOfUsers; i++) {
            UserGameStatus userGameStatus = new UserGameStatus();
            seats.get(i).setUserGameStatus(userGameStatus);
        }

        table.setSeats(seats);
    }

}