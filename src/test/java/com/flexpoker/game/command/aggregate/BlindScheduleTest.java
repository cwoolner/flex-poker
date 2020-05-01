package com.flexpoker.game.command.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BlindScheduleTest {

    @Test
    void testInitial() {
        var blindSchedule = new BlindSchedule(10);
        assertEquals(1, blindSchedule.getCurrentLevel());
        assertEquals(10, blindSchedule.getNumberOfMinutesBetweenLevels());
        assertEquals(10, blindSchedule.getCurrentBlindAmounts().getSmallBlind());
        assertEquals(20, blindSchedule.getCurrentBlindAmounts().getBigBlind());
    }

    @Test
    void testIncrement() {
        var blindSchedule = new BlindSchedule(10);
        blindSchedule.incrementLevel();
        assertFalse(blindSchedule.isMaxLevel());
        assertEquals(2, blindSchedule.getCurrentLevel());
        assertEquals(20, blindSchedule.getCurrentBlindAmounts().getSmallBlind());
        assertEquals(40, blindSchedule.getCurrentBlindAmounts().getBigBlind());
    }

    @Test
    void testMaxLevel() {
        var blindSchedule = new BlindSchedule(10);
        blindSchedule.incrementLevel();
        blindSchedule.incrementLevel();
        blindSchedule.incrementLevel();
        blindSchedule.incrementLevel();
        assertTrue(blindSchedule.isMaxLevel());
        assertEquals(5, blindSchedule.getCurrentLevel());

        blindSchedule.incrementLevel();
        assertEquals(5, blindSchedule.getCurrentLevel());
    }
    
    // TODO: re-enable some of these tests once a blind schedule editor is added
    /*
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
    */


}
