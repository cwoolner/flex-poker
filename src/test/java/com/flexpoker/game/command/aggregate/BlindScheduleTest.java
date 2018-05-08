package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlindScheduleTest {

    @Test
    public void testInitial() {
        var blindSchedule = new BlindSchedule(10);
        assertEquals(1, blindSchedule.getCurrentLevel());
        assertEquals(10, blindSchedule.getNumberOfMinutesBetweenLevels());
        assertEquals(10, blindSchedule.getCurrentBlindAmounts().getSmallBlind());
        assertEquals(20, blindSchedule.getCurrentBlindAmounts().getBigBlind());
    }

    @Test
    public void testIncrement() {
        var blindSchedule = new BlindSchedule(10);
        blindSchedule.incrementLevel();
        assertFalse(blindSchedule.isMaxLevel());
        assertEquals(2, blindSchedule.getCurrentLevel());
        assertEquals(20, blindSchedule.getCurrentBlindAmounts().getSmallBlind());
        assertEquals(40, blindSchedule.getCurrentBlindAmounts().getBigBlind());
    }

    @Test
    public void testMaxLevel() {
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

}
