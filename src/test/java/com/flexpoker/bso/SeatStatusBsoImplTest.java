package com.flexpoker.bso;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.util.ActionOnSeatPredicate;
import com.flexpoker.util.BigBlindSeatPredicate;
import com.flexpoker.util.ButtonSeatPredicate;
import com.flexpoker.util.DataUtilsForTests;
import com.flexpoker.util.SmallBlindSeatPredicate;

public class SeatStatusBsoImplTest {

    private SeatStatusBsoImpl bso;
    
    private ValidationBso mockValidationBso;
    
    private ActionOnTimerBso mockActionOnTimerBso;

    @Before
    public void setup() {
        mockValidationBso = mock(ValidationBso.class);
        mockActionOnTimerBso = mock(ActionOnTimerBso.class);
        bso = new SeatStatusBsoImpl(mockValidationBso, mockActionOnTimerBso);
    }

    @Test
    public void testSetStatusForNewGame() {
        Game game = new Game();
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 2);
        bso.setStatusForNewGame(game, table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());

        Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        Seat bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ActionOnSeatPredicate());

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
        DataUtilsForTests.fillTableWithUsers(table, 3);
        bso.setStatusForNewGame(game, table);

        assertTrue(table.getSeats().get(0).isStillInHand());
        assertTrue(table.getSeats().get(1).isStillInHand());
        assertTrue(table.getSeats().get(2).isStillInHand());

        buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
        actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ActionOnSeatPredicate());

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
        DataUtilsForTests.fillTableWithUsers(table, 6);
        bso.setStatusForNewGame(game, table);

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
        actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ActionOnSeatPredicate());

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

    @Test
    public void testStatusForNewHand() {
        setStatusForNewHandHelper(2, Arrays.asList(new Integer[]{}), 1, 1, 0, 1);
        setStatusForNewHandHelper(3, Arrays.asList(new Integer[]{0}), 2, 2, 1, 2);
        setStatusForNewHandHelper(3, Arrays.asList(new Integer[]{1}), 2, 2, 0, 2);
        setStatusForNewHandHelper(3, Arrays.asList(new Integer[]{2}), 1, 1, 0, 1);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{0}), 1, 2, 3, 1);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{1}), 1, 2, 3, 0);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{2}), 1, 2, 3, 0);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{3}), 1, 2, 0, 1);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{0, 1}), 2, 2, 3, 2);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{1, 2}), 0, 0, 3, 0);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{2, 3}), 1, 1, 0, 1);
        setStatusForNewHandHelper(4, Arrays.asList(new Integer[]{0, 3}), 2, 2, 1, 2);
        setStatusForNewHandHelper(5, Arrays.asList(new Integer[]{}), 1, 2, 3, 4);
        setStatusForNewHandHelper(5, Arrays.asList(new Integer[]{2}), 1, 2, 3, 4);
        setStatusForNewHandHelper(5, Arrays.asList(new Integer[]{2, 3}), 1, 2, 4, 0);
        setStatusForNewHandHelper(5, Arrays.asList(new Integer[]{0, 1, 2}), 4, 4, 3, 4);
        setStatusForNewHandHelper(5, Arrays.asList(new Integer[]{0, 1, 4}), 2, 2, 3, 2);
    }

    @Test
    public void testStatusForNewRound() {
        setStatusForNewRoundHelper(2, Arrays.asList(new Integer[]{}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(3, Arrays.asList(new Integer[]{0}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(3, Arrays.asList(new Integer[]{1}), Arrays.asList(new Integer[]{}), 2);
        setStatusForNewRoundHelper(3, Arrays.asList(new Integer[]{2}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{0}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{1}), Arrays.asList(new Integer[]{}), 2);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{2}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{3}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{0, 1}), Arrays.asList(new Integer[]{}), 2);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{1, 2}), Arrays.asList(new Integer[]{}), 3);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{2, 3}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{0, 3}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{2}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{2, 3}), Arrays.asList(new Integer[]{}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{0, 1, 2}), Arrays.asList(new Integer[]{}), 3);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{0, 1, 4}), Arrays.asList(new Integer[]{}), 2);

        // add some users that have dropped out during the hand, but not necessarily left the table
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{0}), Arrays.asList(new Integer[]{1}), 2);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{1}), Arrays.asList(new Integer[]{2}), 3);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{2}), Arrays.asList(new Integer[]{0}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[]{3}), Arrays.asList(new Integer[]{1}), 2);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{}), Arrays.asList(new Integer[]{0, 1, 2}), 3);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{2}), Arrays.asList(new Integer[]{0, 1}), 3);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[]{2, 3}), Arrays.asList(new Integer[]{1}), 4);
    }

    private void setStatusForNewHandHelper(int numberOfPlayers,
            List<Integer> seatsThatJustLeft, int buttonIndex,
            int smallBlindIndex, int bigBlindIndex, int actionOnIndex) {
        Game game = new Game();
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, numberOfPlayers);
        table.getSeats().get(0).setButton(true);
        table.getSeats().get(1).setSmallBlind(true);
        table.getSeats().get(2).setBigBlind(true);

        for (Integer seat : seatsThatJustLeft) {
            table.getSeats().get(seat.intValue()).setUserGameStatus(null);
            table.getSeats().get(seat.intValue()).setPlayerJustLeft(true);
        }

        bso.setStatusForNewHand(game, table);

        for (int i = 0; i < numberOfPlayers; i++) {
            if (seatsThatJustLeft.contains(i)) {
                assertFalse(table.getSeats().get(i).isStillInHand());
            } else {
                assertTrue(table.getSeats().get(i).isStillInHand());
            }
        }

        Seat buttonSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ButtonSeatPredicate());
        Seat smallBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new SmallBlindSeatPredicate());
        Seat bigBlindSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new BigBlindSeatPredicate());
        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ActionOnSeatPredicate());

        assertTrue(buttonSeat.equals(table.getSeats().get(buttonIndex)));
        assertTrue(smallBlindSeat.equals(table.getSeats().get(smallBlindIndex)));
        assertTrue(bigBlindSeat.equals(table.getSeats().get(bigBlindIndex)));
        assertTrue(actionOnSeat.equals(table.getSeats().get(actionOnIndex)));
    }

    private void setStatusForNewRoundHelper(int numberOfPlayers,
            List<Integer> seatsThatJustLeft, List<Integer> seatsNotInHand,
            int actionOnIndex) {
        Game game = new Game();
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, numberOfPlayers);
        table.getSeats().get(0).setButton(true);
        table.getSeats().get(1).setSmallBlind(true);
        table.getSeats().get(2).setBigBlind(true);
        table.getSeats().get(3).setActionOn(true);

        for (Integer seat : seatsThatJustLeft) {
            table.getSeats().get(seat.intValue()).setUserGameStatus(null);
            table.getSeats().get(seat.intValue()).setPlayerJustLeft(true);
        }

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null) {
                seat.setStillInHand(true);
            }
        }

        for (Integer seat : seatsNotInHand) {
            table.getSeats().get(seat.intValue()).setStillInHand(false);
        }

        bso.setStatusForNewRound(game, table);
        Seat actionOnSeat = (Seat) CollectionUtils.find(table.getSeats(),
                new ActionOnSeatPredicate());
        assertTrue(actionOnSeat.equals(table.getSeats().get(actionOnIndex)));
    }

}
