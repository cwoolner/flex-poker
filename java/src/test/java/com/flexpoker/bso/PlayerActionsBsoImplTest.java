package com.flexpoker.bso;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.util.Context;

public class PlayerActionsBsoImplTest {

    private PlayerActionsBsoImpl bso = (PlayerActionsBsoImpl) Context.instance()
            .getBean("playerActionsBso");

    @Test
    public void testCheck() {
        assertTrue(true);
    }

    @Test
    public void testFold() {
        assertTrue(true);
    }

    @Test
    public void testCall() {
        assertTrue(true);
    }

    @Test
    public void testRaise() {
        assertTrue(true);
    }

}
