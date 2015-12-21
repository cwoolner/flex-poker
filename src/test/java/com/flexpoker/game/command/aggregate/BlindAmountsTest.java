package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BlindAmountsTest {

    @Test
    public void testNormalSuccess() {
        BlindAmounts blinds = new BlindAmounts(10, 20);
        assertEquals(10, blinds.getSmallBlind());
        assertEquals(20, blinds.getBigBlind());
    }

    @Test
    public void testLowNormalSuccess() {
        BlindAmounts blinds = new BlindAmounts(1, 2);
        assertEquals(1, blinds.getSmallBlind());
        assertEquals(2, blinds.getBigBlind());
    }

    @Test
    public void testMaxValueSuccess() {
        BlindAmounts blinds = new BlindAmounts(Integer.MAX_VALUE / 2, Integer.MAX_VALUE - 1);
        assertEquals(Integer.MAX_VALUE / 2, blinds.getSmallBlind());
        assertEquals(Integer.MAX_VALUE - 1, blinds.getBigBlind());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testBigIsDoubleSmallFail() {
        new BlindAmounts(10, 19);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testSmallBlindTooLarge() {
        new BlindAmounts((Integer.MAX_VALUE / 2) + 1, Integer.MAX_VALUE);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testSmallBlindCannotBeZero() {
        new BlindAmounts(0, 1);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testBigBlindCannotBeZero() {
        new BlindAmounts(1, 0);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testSmallBlindTooSmall() {
        new BlindAmounts(-1, 0);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testLargeBlindTooSmall() {
        new BlindAmounts(0, -1);
    }

}
