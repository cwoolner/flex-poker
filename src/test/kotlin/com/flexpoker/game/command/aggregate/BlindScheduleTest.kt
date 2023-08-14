package com.flexpoker.game.command.aggregate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BlindScheduleTest {

    @Test
    fun testInitial() {
        val blindSchedule = BlindSchedule.init(10)
        assertEquals(1, blindSchedule.currentLevel)
        assertEquals(10, blindSchedule.numberOfMinutesBetweenLevels)
        assertEquals(10, blindSchedule.currentBlinds().smallBlind)
        assertEquals(20, blindSchedule.currentBlinds().bigBlind)
    }

    @Test
    fun testIncrement() {
        val blindSchedule = BlindSchedule.init(10).incrementBlinds()
        assertEquals(2, blindSchedule.currentLevel)
        assertEquals(20, blindSchedule.currentBlinds().smallBlind)
        assertEquals(40, blindSchedule.currentBlinds().bigBlind)
    }

    @Test
    fun testMaxLevel() {
        val blindSchedule = BlindSchedule.init(10).incrementBlinds().incrementBlinds().incrementBlinds().incrementBlinds()
        assertTrue(blindSchedule.atMaxLevel())
        assertEquals(5, blindSchedule.currentLevel)
        assertEquals(5, blindSchedule.incrementBlinds().currentLevel)
    }

    @Test
    fun testCurrentLevelBounds() {
        assertThrows(IllegalArgumentException::class.java) { BlindSchedule(10, 0) }
        assertThrows(IllegalArgumentException::class.java) { BlindSchedule(10, 6) }
    }

}
