package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.HashMap
import java.util.HashSet
import java.util.UUID

class CalculatePotsPotHandlerTest {

    @Test
    fun testTwoPlayersSameBet() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val chipsInFrontMap = HashMap<UUID, Int>()
        chipsInFrontMap[player1] = 10
        chipsInFrontMap[player2] = 10
        val chipsInBackMap = HashMap<UUID, Int>()
        chipsInBackMap[player1] = 1490
        chipsInBackMap[player2] = 1490
        val playersStillInHand = HashSet<UUID>()
        playersStillInHand.add(player1)
        playersStillInHand.add(player2)
        val potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap)
        verifyNumberOfEventsAndEntireOrderByType(
            potEvents,
            PotCreatedEvent::class.java,
            PotAmountIncreasedEvent::class.java
        )
        assertTrue((potEvents[0] as PotCreatedEvent).potId == (potEvents[1] as PotAmountIncreasedEvent).potId)
        assertEquals(20, (potEvents[1] as PotAmountIncreasedEvent).amountIncreased)
    }

    @Test
    fun testTwoPlayerDifferentBets() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val chipsInFrontMap = HashMap<UUID, Int>()
        chipsInFrontMap[player1] = 10
        chipsInFrontMap[player2] = 20
        val chipsInBackMap = HashMap<UUID, Int>()
        chipsInBackMap[player1] = 1490
        chipsInBackMap[player2] = 1480
        val playersStillInHand = HashSet<UUID>()
        playersStillInHand.add(player1)
        playersStillInHand.add(player2)
        val potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap)
        verifyNumberOfEventsAndEntireOrderByType(
            potEvents, PotCreatedEvent::class.java, PotAmountIncreasedEvent::class.java,
            PotAmountIncreasedEvent::class.java
        )
        assertEquals(20, (potEvents[1] as PotAmountIncreasedEvent).amountIncreased)
        assertEquals(10, (potEvents[2] as PotAmountIncreasedEvent).amountIncreased)
        assertTrue((potEvents[0] as PotCreatedEvent).potId == (potEvents[1] as PotAmountIncreasedEvent).potId)
    }

    @Test
    fun testTwoPlayerOneAllIn() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val chipsInFrontMap = HashMap<UUID, Int>()
        chipsInFrontMap[player1] = 1000
        chipsInFrontMap[player2] = 20
        val chipsInBackMap = HashMap<UUID, Int>()
        chipsInBackMap[player1] = 500
        chipsInBackMap[player2] = 0
        val playersStillInHand = HashSet<UUID>()
        playersStillInHand.add(player1)
        playersStillInHand.add(player2)
        val potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap)
        verifyNumberOfEventsAndEntireOrderByType(
            potEvents,
            PotCreatedEvent::class.java,
            PotAmountIncreasedEvent::class.java,
            PotClosedEvent::class.java,
            PotCreatedEvent::class.java,
            PotAmountIncreasedEvent::class.java
        )
        assertTrue((potEvents[0] as PotCreatedEvent).playersInvolved.contains(player1))
        assertTrue((potEvents[0] as PotCreatedEvent).playersInvolved.contains(player2))
        assertEquals(40, (potEvents[1] as PotAmountIncreasedEvent).amountIncreased)
        assertTrue((potEvents[3] as PotCreatedEvent).playersInvolved.contains(player1))
        assertFalse((potEvents[3] as PotCreatedEvent).playersInvolved.contains(player2))
        assertEquals(980, (potEvents[4] as PotAmountIncreasedEvent).amountIncreased)
        assertTrue((potEvents[0] as PotCreatedEvent).potId == (potEvents[1] as PotAmountIncreasedEvent).potId)
    }

    @Test
    fun testTwoPlayerChipAndAChair() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val chipsInFrontMap = HashMap<UUID, Int>()
        chipsInFrontMap[player1] = 2
        chipsInFrontMap[player2] = 1
        val chipsInBackMap = HashMap<UUID, Int>()
        chipsInBackMap[player1] = 1499
        chipsInBackMap[player2] = 0
        val playersStillInHand = HashSet<UUID>()
        playersStillInHand.add(player1)
        playersStillInHand.add(player2)
        val potEvents = potHandler.calculatePots(chipsInFrontMap, chipsInBackMap)
        verifyNumberOfEventsAndEntireOrderByType(
            potEvents,
            PotCreatedEvent::class.java,
            PotAmountIncreasedEvent::class.java,
            PotClosedEvent::class.java,
            PotCreatedEvent::class.java,
            PotAmountIncreasedEvent::class.java
        )
        assertTrue((potEvents[0] as PotCreatedEvent).playersInvolved.contains(player1))
        assertTrue((potEvents[0] as PotCreatedEvent).playersInvolved.contains(player2))
        assertEquals(2, (potEvents[1] as PotAmountIncreasedEvent).amountIncreased)
        assertTrue((potEvents[3] as PotCreatedEvent).playersInvolved.contains(player1))
        assertFalse((potEvents[3] as PotCreatedEvent).playersInvolved.contains(player2))
        assertEquals(1, (potEvents[4] as PotAmountIncreasedEvent).amountIncreased)
        assertTrue((potEvents[0] as PotCreatedEvent).potId == (potEvents[1] as PotAmountIncreasedEvent).potId)
    }
}