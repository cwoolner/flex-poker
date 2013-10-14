package com.flexpoker.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TableTest {

    @Test
    public void testValidateTable() {
        Table table = new Table();

        try {
            table.validateTable();
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        table.addSeat(seat1);

        try {
            table.validateTable();
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        Seat seat2 = new Seat();
        seat2.setPosition(1);
        table.addSeat(seat1);

        try {
            table.validateTable();
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        seat1.setUserGameStatus(new UserGameStatus());
        seat2.setUserGameStatus(new UserGameStatus());

        table.validateTable();
    }

}
