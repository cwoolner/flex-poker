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

}
