package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.GameStage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class BlindScheduleTest {

    private fun basicGameState(numberOfMinutesBetwenLevels: Int): GameState {
        return GameState(UUID.randomUUID(), 2, 2,
            GameStage.REGISTERING, blindSchedule(10))
    }

    @Test
    fun testInitial() {
        val blindSchedule = blindSchedule(10)
        assertEquals(1, blindSchedule.currentLevel)
        assertEquals(10, blindSchedule.numberOfMinutesBetweenLevels)
        assertEquals(10, blindSchedule.levelToAmountsMap[1]!!.smallBlind)
        assertEquals(20, blindSchedule.levelToAmountsMap[1]!!.bigBlind)
    }

    @Test
    fun testIncrement() {
        val state = basicGameState(10)
        val blindSchedule = incrementLevel(state).blindScheduleDTO
        assertEquals(2, blindSchedule.currentLevel)
        assertEquals(20, blindSchedule.levelToAmountsMap[2]!!.smallBlind)
        assertEquals(40, blindSchedule.levelToAmountsMap[2]!!.bigBlind)
    }

    @Test
    fun testMaxLevel() {
        val state = basicGameState(10)
        val updatedState = incrementLevel(incrementLevel(incrementLevel(incrementLevel(state))))
        assertTrue(isMaxLevel(updatedState))
        assertEquals(5, updatedState.blindScheduleDTO.currentLevel)
        val maxState = incrementLevel(updatedState)
        assertEquals(5, maxState.blindScheduleDTO.currentLevel)
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