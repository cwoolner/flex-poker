package com.flexpoker.util;

import java.util.HashSet;
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

    public static void fillTableWithUsers(Table table, int numberOfUsers, int maxPlayersPerTable) {
        for (int i = 0; i < maxPlayersPerTable; i++) {
            Seat seat = new Seat(1);
            table.addSeat(seat);
        }

        for (int i = 0; i < numberOfUsers; i++) {
            UserGameStatus userGameStatus = new UserGameStatus();
            table.getSeats().get(i).setUserGameStatus(userGameStatus);
        }
    }


    
}
