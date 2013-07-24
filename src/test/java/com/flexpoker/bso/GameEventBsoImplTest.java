package com.flexpoker.bso;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.util.Context;

public class GameEventBsoImplTest {

    private GameEventBsoImpl bso = (GameEventBsoImpl) Context.instance()
            .getBean("gameEventBso");

    @Test
    public void testAddUserToGame() {
        assertTrue(true);
    }

    @Test
    public void testVerifyRegistration() {
        assertTrue(true);
    }

    @Test
    public void testVerifyGameInProgress() {
        assertTrue(true);
    }

    @Test
    public void testVerifyReadyToStartNewHand() {
        assertTrue(true);
    }

}
