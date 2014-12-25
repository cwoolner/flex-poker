package com.flexpoker.core.seatstatus;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.test.util.datageneration.GameGenerator;
import com.flexpoker.util.DataUtilsForTests;

public class SetSeatStatusForNewGameImplCommandTest {

    private SetSeatStatusForNewGameImplCommand command;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SetSeatStatusForNewGameImplCommand();
    }

    @Test
    public void testExecute() {
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 2, 9);
        command.execute(GameGenerator.createGame(9, 9), table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());

        Seat buttonSeat = table.getButtonSeat();
        Seat smallBlindSeat = table.getSmallBlindSeat();
        Seat bigBlindSeat = table.getBigBlindSeat();
        Seat actionOnSeat = table.getActionOnSeat();

        // since things are assigned randomly, need to do some if/else logic
        if (buttonSeat.equals(table.getSeats().get(0))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(0)));
        } else if (buttonSeat.equals(table.getSeats().get(1))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(1)));
        } else {
            fail("None of the seats were the button.");
        }

        table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 3, 9);
        command.execute(GameGenerator.createGame(9, 9), table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());
        assertTrue(table.getSeats().get(2).isStillInHand());

        buttonSeat = table.getButtonSeat();
        smallBlindSeat = table.getSmallBlindSeat();
        bigBlindSeat = table.getBigBlindSeat();
        actionOnSeat = table.getActionOnSeat();

        // since things are assigned randomly, need to do some if/else logic
        if (buttonSeat.equals(table.getSeats().get(0))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(2)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(0)));
        } else if (buttonSeat.equals(table.getSeats().get(1))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(2)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(1)));
        } else if (buttonSeat.equals(table.getSeats().get(2))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(2)));
        } else {
            fail("None of the seats were the button.");
        }

        table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 6, 9);
        command.execute(GameGenerator.createGame(9, 9), table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());
        assertTrue(table.getSeats().get(2).isStillInHand());
        assertTrue(table.getSeats().get(3).isStillInHand());
        assertTrue(table.getSeats().get(4).isStillInHand());
        assertTrue(table.getSeats().get(5).isStillInHand());

        buttonSeat = table.getButtonSeat();
        smallBlindSeat = table.getSmallBlindSeat();
        bigBlindSeat = table.getBigBlindSeat();
        actionOnSeat = table.getActionOnSeat();

        // since things are assigned randomly, need to do some if/else logic
        if (buttonSeat.equals(table.getSeats().get(0))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(2)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(3)));
        } else if (buttonSeat.equals(table.getSeats().get(1))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(2)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(3)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(4)));
        } else if (buttonSeat.equals(table.getSeats().get(2))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(3)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(4)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(5)));
        } else if (buttonSeat.equals(table.getSeats().get(3))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(4)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(5)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(0)));
        } else if (buttonSeat.equals(table.getSeats().get(4))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(5)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(1)));
        } else if (buttonSeat.equals(table.getSeats().get(5))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(2)));
        } else {
            fail("None of the seats were the button.");
        }
    }

}
