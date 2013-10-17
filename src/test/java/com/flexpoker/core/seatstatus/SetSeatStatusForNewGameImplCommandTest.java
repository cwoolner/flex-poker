package com.flexpoker.core.seatstatus;

import static org.junit.Assert.*;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.DataUtilsForTests;
import com.flexpoker.util.SmallBlindSeatPredicate;

public class SetSeatStatusForNewGameImplCommandTest {

    private SetSeatStatusForNewGameImplCommand command;
    
    @Mock private ActionOnTimerBso actionOnTimerBso;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SetSeatStatusForNewGameImplCommand(actionOnTimerBso);
    }
    
    @Test
    public void testExecute() {
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 2, 9);
        command.execute(table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());

        Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        Seat bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
        Seat actionOnSeat = table.getActionOnSeat();

        // since things are assigned randomly, need to do some if/else logic
        if (buttonSeat.equals(table.getSeats().get(0))) {
            assertTrue(smallBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(0)));
        } else if (buttonSeat.equals(table.getSeats().get(1))){
            assertTrue(smallBlindSeat.equals(table.getSeats().get(1)));
            assertTrue(bigBlindSeat.equals(table.getSeats().get(0)));
            assertTrue(actionOnSeat.equals(table.getSeats().get(1)));
        } else {
            fail("None of the seats were the button.");
        }

        table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 3, 9);
        command.execute(table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());
        assertTrue(table.getSeats().get(2).isStillInHand());

        buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
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
        command.execute(table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());
        assertTrue(table.getSeats().get(2).isStillInHand());
        assertTrue(table.getSeats().get(3).isStillInHand());
        assertTrue(table.getSeats().get(4).isStillInHand());
        assertTrue(table.getSeats().get(5).isStillInHand());

        buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
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
