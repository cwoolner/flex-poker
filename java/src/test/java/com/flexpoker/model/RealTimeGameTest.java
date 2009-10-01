package com.flexpoker.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class RealTimeGameTest {

    @Test
    public void testIsEventVerified() {
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

}
