package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.Context;


public class SeatStatusBsoImplTest {

    private SeatStatusBsoImpl bso = (SeatStatusBsoImpl) Context.instance()
            .getBean("seatStatusBso");

    @Test
    public void testSetStatusForNewGame() {
        Table table = new Table();
        List<Seat> seats = new ArrayList<Seat>();
        table.setSeats(seats);

        Seat seat1 = new Seat();
        seat1.setPosition(0);
        seat1.setUserGameStatus(new UserGameStatus());

        Seat seat2 = new Seat();
        seat2.setPosition(1);
        seat2.setUserGameStatus(new UserGameStatus());

        seats.add(seat1);
        seats.add(seat2);

        bso.setStatusForNewGame(table);

        assertTrue(table.getButton().getPosition() == 0
                || table.getButton().getPosition() == 1);
    }

}
