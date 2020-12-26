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
        val hands = createPotHands(player1, player2)
        val pots = mutableSetOf<Pot>()
        addNewPot(pots, hands, potId1, playersInvolved)
        addNewPot(pots, hands, potId2, playersInvolved)
        addToPot(pots, potId1, 20)
        addToPot(pots, potId2, 10)
        val playersRequiredToShowCards = fetchPlayersRequiredToShowCards(pots, playersInvolved)
        val fetchChipsWon = fetchChipsWon(pots, playersInvolved)
        assertTrue(playersRequiredToShowCards.contains(player1))
        assertFalse(playersRequiredToShowCards.contains(player2))
        assertEquals(30, fetchChipsWon[player1]!!.toInt())
    }

    @Test
    fun testAddNewPotWithPlayersThatDontExist() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val pots = mutableSetOf<Pot>()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        assertThrows(IllegalArgumentException::class.java) { addNewPot(pots, hands, potId, setOf(player3)) }
    }

    @Test
    fun testClosePot() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val pots = mutableSetOf<Pot>()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        addNewPot(pots, hands, potId, setOf(player1, player2))
        closePot(pots, potId)
    }

    @Test
    fun testClosePotThatDoesntExist() {
        val pots = mutableSetOf<Pot>()
        val hands = createPotHands(UUID.randomUUID(), UUID.randomUUID())
        assertThrows(NoSuchElementException::class.java) { closePot(pots, UUID.randomUUID()) }
    }

    @Test
    fun testAddChipsToClosedPot() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val pots = mutableSetOf<Pot>()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        addNewPot(pots, hands, potId, setOf(player1, player2))
        closePot(pots, potId)
        assertThrows(FlexPokerException::class.java) { addToPot(pots, potId, 10) }
    }

}