package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.exception.FlexPokerException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.NoSuchElementException
import java.util.UUID

class BasicPotHandlerTest {

    @Test
    fun testTwoPlayersRequiredToShowCardsAndChipsWon() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potId1 = UUID.randomUUID()
        val potId2 = UUID.randomUUID()
        val playersInvolved = setOf(player1, player2)
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        potHandler.addNewPot(potId1, playersInvolved)
        potHandler.addNewPot(potId2, playersInvolved)
        potHandler.addToPot(potId1, 20)
        potHandler.addToPot(potId2, 10)
        val playersRequiredToShowCards = potHandler.fetchPlayersRequiredToShowCards(playersInvolved)
        val fetchChipsWon = potHandler.fetchChipsWon(playersInvolved)
        assertTrue(playersRequiredToShowCards.contains(player1))
        assertFalse(playersRequiredToShowCards.contains(player2))
        assertEquals(30, fetchChipsWon[player1]!!.toInt())
    }

    @Test
    fun testAddNewPotWithPlayersThatDontExist() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val potId = UUID.randomUUID()
        assertThrows(IllegalArgumentException::class.java) { potHandler.addNewPot(potId, setOf(player3)) }
    }

    @Test
    fun testClosePot() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val potId = UUID.randomUUID()
        potHandler.addNewPot(potId, setOf(player1, player2))
        potHandler.closePot(potId)
    }

    @Test
    fun testClosePotThatDoesntExist() {
        val potHandler = PotTestUtils.createBasicPotHandler(UUID.randomUUID(), UUID.randomUUID())
        assertThrows(NoSuchElementException::class.java) { potHandler.closePot(UUID.randomUUID()) }
    }

    @Test
    fun testAddChipsToClosedPot() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val potHandler = PotTestUtils.createBasicPotHandler(player1, player2)
        val potId = UUID.randomUUID()
        potHandler.addNewPot(potId, setOf(player1, player2))
        potHandler.closePot(potId)
        assertThrows(FlexPokerException::class.java) { potHandler.addToPot(potId, 10) }
    }

}