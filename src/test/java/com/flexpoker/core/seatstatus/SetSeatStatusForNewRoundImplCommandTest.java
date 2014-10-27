package com.flexpoker.core.seatstatus;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.core.api.scheduling.ScheduleAndReturnActionOnTimerCommand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.test.util.datageneration.GameGenerator;
import com.flexpoker.util.DataUtilsForTests;

public class SetSeatStatusForNewRoundImplCommandTest {

    private SetSeatStatusForNewRoundImplCommand command;

    @Mock
    ScheduleAndReturnActionOnTimerCommand createAndStartActionOnTimerCommand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        command = new SetSeatStatusForNewRoundImplCommand(
                createAndStartActionOnTimerCommand);
    }

    @SuppressWarnings("boxing")
    @Test
    public void testExecute() {
        setStatusForNewRoundHelper(2, Arrays.asList(new Integer[] {}),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(3, Arrays.asList(new Integer[] { 0 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(3, Arrays.asList(new Integer[] { 1 }),
                Arrays.asList(new Integer[] {}), 2);
        setStatusForNewRoundHelper(3, Arrays.asList(new Integer[] { 2 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 0 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 1 }),
                Arrays.asList(new Integer[] {}), 2);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 2 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 3 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 0, 1 }),
                Arrays.asList(new Integer[] {}), 2);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 1, 2 }),
                Arrays.asList(new Integer[] {}), 3);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 2, 3 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 0, 3 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] {}),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] { 2 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] { 2, 3 }),
                Arrays.asList(new Integer[] {}), 1);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] { 0, 1, 2 }),
                Arrays.asList(new Integer[] {}), 3);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] { 0, 1, 4 }),
                Arrays.asList(new Integer[] {}), 2);

        // add some users that have dropped out during the hand, but not
        // necessarily left the table
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 0 }),
                Arrays.asList(new Integer[] { 1 }), 2);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 1 }),
                Arrays.asList(new Integer[] { 2 }), 3);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 2 }),
                Arrays.asList(new Integer[] { 0 }), 1);
        setStatusForNewRoundHelper(4, Arrays.asList(new Integer[] { 3 }),
                Arrays.asList(new Integer[] { 1 }), 2);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] {}),
                Arrays.asList(new Integer[] { 0, 1, 2 }), 3);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] { 2 }),
                Arrays.asList(new Integer[] { 0, 1 }), 3);
        setStatusForNewRoundHelper(5, Arrays.asList(new Integer[] { 2, 3 }),
                Arrays.asList(new Integer[] { 1 }), 4);
    }

    private void setStatusForNewRoundHelper(int numberOfPlayers,
            List<Integer> seatsThatJustLeft, List<Integer> seatsNotInHand,
            int actionOnIndex) {
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, numberOfPlayers, 9);
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

        command.execute(GameGenerator.createGame(9, 9), table);
        Seat actionOnSeat = table.getActionOnSeat();
        assertTrue(actionOnSeat.equals(table.getSeats().get(actionOnIndex)));
    }

}
