package com.flexpoker.game.command.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BlindAmountsTest {

    @Test
    void testNormalSuccess() {
        var blinds = new BlindAmounts(10, 20);
        assertEquals(10, blinds.getSmallBlind());
        assertEquals(20, blinds.getBigBlind());
    }

    @Test
    void testLowNormalSuccess() {
        var blinds = new BlindAmounts(1, 2);
        assertEquals(1, blinds.getSmallBlind());
        assertEquals(2, blinds.getBigBlind());
    }

    @Test
    void testMaxValueSuccess() {
        var blinds = new BlindAmounts(Integer.MAX_VALUE / 2, Integer.MAX_VALUE - 1);
        assertEquals(Integer.MAX_VALUE / 2, blinds.getSmallBlind());
        assertEquals(Integer.MAX_VALUE - 1, blinds.getBigBlind());
    }

    @Test
    void testBigIsDoubleSmallFail() {
        assertThrows(IllegalArgumentException.class, () -> new BlindAmounts(10, 19));
    }

    @Test
    void testSmallBlindTooLarge() {
        assertThrows(IllegalArgumentException.class,
                () -> new BlindAmounts((Integer.MAX_VALUE / 2) + 1, Integer.MAX_VALUE));
    }

    @Test
    void testSmallBlindCannotBeZero() {
        assertThrows(IllegalArgumentException.class, () -> new BlindAmounts(0, 1));
    }

    @Test
    void testBigBlindCannotBeZero() {
        assertThrows(IllegalArgumentException.class, () -> new BlindAmounts(1, 0));
    }

    @Test
    void testSmallBlindTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> new BlindAmounts(-1, 0));
    }

    @Test
    void testLargeBlindTooSmall() {
        assertThrows(IllegalArgumentException.class, () -> new BlindAmounts(0, -1));
    }

}
