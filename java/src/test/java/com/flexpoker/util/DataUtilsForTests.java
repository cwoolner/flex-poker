package com.flexpoker.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;


public class DataUtilsForTests {

    public static Set<UserGameStatus> createUserGameStatusSet(int numberOfUserGameStatuses) {
        Set<UserGameStatus> userGameStatuses = new HashSet<UserGameStatus>();

        for (int i = 0; i < numberOfUserGameStatuses; i++) {
            userGameStatuses.add(new UserGameStatus());
        }

        return userGameStatuses;
    }

    public static void fillTableWithUsers(Table table, int numberOfUsers) {
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
