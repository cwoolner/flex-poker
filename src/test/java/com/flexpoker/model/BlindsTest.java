package com.flexpoker.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BlindsTest {

    @Test
    public void testNormalSuccess() {
        Blinds blinds = new Blinds(10, 20);
        assertEquals(10, blinds.getSmallBlind());
        assertEquals(20, blinds.getBigBlind());
    }

    @Test
    public void testMaxValueSuccess() {
        Blinds blinds = new Blinds(Integer.MAX_VALUE / 2, Integer.MAX_VALUE - 1);
        assertEquals(Integer.MAX_VALUE / 2, blinds.getSmallBlind());
        assertEquals(Integer.MAX_VALUE - 1, blinds.getBigBlind());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testSmallBlindTooLarge() {
        new Blinds((Integer.MAX_VALUE / 2) + 1, Integer.MAX_VALUE);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testSmallBlindCannotBeZero() {
        new Blinds(0, 1);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testBigBlindCannotBeZero() {
        new Blinds(1, 0);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testSmallBlindTooSmall() {
        new Blinds(-1, 0);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testLargeBlindTooSmall() {
        new Blinds(0, -1);
    }

}
