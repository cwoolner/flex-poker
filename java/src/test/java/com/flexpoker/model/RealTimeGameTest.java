package com.flexpoker.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.util.DataUtilsForTests;

public class RealTimeGameTest {

    @Test
    public void testIsEventVerifiedGame() {
        User user1 = new User();
        User user2 = new User();
        UserGameStatus userGameStatus1 = new UserGameStatus();
        userGameStatus1.setUser(user1);
        UserGameStatus userGameStatus2 = new UserGameStatus();
        userGameStatus2.setUser(user2);

        RealTimeGame realTimeGame = new RealTimeGame();
        realTimeGame.addUserGameStatus(userGameStatus1);
        realTimeGame.addUserGameStatus(userGameStatus2);
        assertFalse(realTimeGame.isEventVerified("event1"));

        realTimeGame.verifyEvent(user1, "event1");
        assertFalse(realTimeGame.isEventVerified("event1"));

        realTimeGame.verifyEvent(user2, "event1");
        assertTrue(realTimeGame.isEventVerified("event1"));
    }

    @Test
    public void testIsEventVerifiedTable() {
        Table table = new Table();
        DataUtilsForTests.fillTableWithUsers(table, 3);

        RealTimeGame realTimeGame = new RealTimeGame();
        assertFalse(realTimeGame.isEventVerified(table, "event1"));

        User user1 = table.getSeats().get(0).getUserGameStatus().getUser();
        User user2 = table.getSeats().get(0).getUserGameStatus().getUser();
        User user3 = table.getSeats().get(0).getUserGameStatus().getUser();

        realTimeGame.verifyEvent(user1, table, "event1");
        assertFalse(realTimeGame.isEventVerified(table, "event1"));
        realTimeGame.verifyEvent(user2, table, "event1");
        assertFalse(realTimeGame.isEventVerified(table, "event1"));
        realTimeGame.verifyEvent(user3, table, "event1");
        assertTrue(realTimeGame.isEventVerified(table, "event1"));

        assertFalse(realTimeGame.isEventVerified(table, "event2"));
    }

}
