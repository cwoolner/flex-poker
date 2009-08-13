package com.flexpoker.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class RealTimeGameTest {

    @Test
    public void testIsEventVerified() {
        List<User> users = new ArrayList<User>();
        User user1 = new User();
        User user2 = new User();

        users.add(user1);
        users.add(user2);

        RealTimeGame realTimeGame = new RealTimeGame(users);
        assertFalse(realTimeGame.isEventVerified("event1"));

        realTimeGame.verifyEvent(user1, "event1");
        assertFalse(realTimeGame.isEventVerified("event1"));

        realTimeGame.verifyEvent(user2, "event1");
        assertTrue(realTimeGame.isEventVerified("event1"));
    }

}
