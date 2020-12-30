package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.aggregate.addNewPot
import com.flexpoker.table.command.aggregate.addToPot
import com.flexpoker.table.command.aggregate.closePot
import com.flexpoker.table.command.aggregate.fetchChipsWon
import com.flexpoker.table.command.aggregate.fetchPlayersRequiredToShowCards
import com.flexpoker.table.command.aggregate.removePlayerFromAllPots
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePSet
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
        var pots = addNewPot(HashTreePSet.empty(), hands, potId1, playersInvolved)
        pots = addNewPot(pots, hands, potId2, playersInvolved)
        pots = addToPot(pots, potId1, 20)
        pots = addToPot(pots, potId2, 10)
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
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        assertThrows(IllegalArgumentException::class.java) { addNewPot(HashTreePSet.empty(), hands, potId, setOf(player3)) }
    }

    @Test
    fun testClosePot() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), hands, potId, setOf(player1, player2))
        pots = closePot(pots, potId)
        assertFalse(pots.first { it.id == potId }.isOpen)
    }

    @Test
    fun testClosePotThatDoesntExist() {
        assertThrows(IllegalArgumentException::class.java) { closePot(HashTreePSet.empty(), UUID.randomUUID()) }
    }

    @Test
    fun testAddChipsToClosedPot() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), hands, potId, setOf(player1, player2))
        pots = closePot(pots, potId)
        assertThrows(IllegalArgumentException::class.java) { addToPot(pots, potId, 10) }
    }

    @Test
    fun testPotIsOpenOnCreate() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), hands, potId, setOf(player1, player2))
        assertEquals(1, pots.size)
        assertTrue(pots.first().isOpen)
    }

    @Test
    fun testPotCannotHaveAPlayerRemovedAfterClosing() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), hands, potId, setOf(player1, player2))
        pots = closePot(pots, potId)
        assertThrows(IllegalArgumentException::class.java) { removePlayerFromAllPots(pots, player1) }
    }

    @Test
    fun testPotPlayerRemovedHitsRequire() {
        val player1 = UUID.randomUUID()
        val hands = createPotHand(player1)
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), listOf(hands), potId, setOf(player1))
        pots = closePot(pots, potId)
        assertThrows(IllegalArgumentException::class.java) { removePlayerFromAllPots(pots, player1) }
    }

    @Test
    fun testPotCanHaveAPlayerRemoved() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val hands = createPotHands(player1, player2)
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), hands, potId, setOf(player1, player2))
        pots = removePlayerFromAllPots(pots, player1)
        val p1Pot = pots.first().handEvaluations.firstOrNull { it.playerId == player1 }
        val p2Pot = pots.first().handEvaluations.firstOrNull { it.playerId == player2 }
        assertNull(p1Pot)
        assertNotNull(p2Pot)
    }

}