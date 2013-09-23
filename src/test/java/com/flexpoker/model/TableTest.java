package com.flexpoker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TableTest {

    @Test
    public void testValidateTable() {
        Table table = new Table();
        List<Seat> seats = new ArrayList<Seat>();
        table.setSeats(seats);

        try {
            table.validateTable();
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seats.add(seat1);

        try {
            table.validateTable();
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seats.add(seat2);

        try {
            table.validateTable();
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        seat1.setUserGameStatus(new UserGameStatus());
        seat2.setUserGameStatus(new UserGameStatus());

        table.validateTable();
    }

}
