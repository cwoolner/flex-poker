package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BlindScheduleTest {

    @Test
    public void testInitial() {
        BlindSchedule blindSchedule = new BlindSchedule(10);
        assertEquals(1, blindSchedule.getCurrentLevel());
        assertEquals(10, blindSchedule.getNumberOfMinutesBetweenLevels());
        assertEquals(10, blindSchedule.getCurrentBlindAmounts().getSmallBlind());
        assertEquals(20, blindSchedule.getCurrentBlindAmounts().getBigBlind());
    }

    @Test
    public void testIncrement() {
        BlindSchedule blindSchedule = new BlindSchedule(10);
        blindSchedule.incrementLevel();
        assertEquals(2, blindSchedule.getCurrentLevel());
        assertEquals(20, blindSchedule.getCurrentBlindAmounts().getSmallBlind());
        assertEquals(40, blindSchedule.getCurrentBlindAmounts().getBigBlind());
    }

    @Test
    public void testMaxLevel() {
        BlindSchedule blindSchedule = new BlindSchedule(10);
        blindSchedule.incrementLevel();
        blindSchedule.incrementLevel();
        blindSchedule.incrementLevel();
        blindSchedule.incrementLevel();
        assertEquals(5, blindSchedule.getCurrentLevel());

        blindSchedule.incrementLevel();
        assertEquals(5, blindSchedule.getCurrentLevel());
    }

}
