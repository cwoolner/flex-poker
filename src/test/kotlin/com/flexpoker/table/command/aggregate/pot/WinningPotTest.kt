package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import com.flexpoker.table.command.aggregate.addNewPot
import com.flexpoker.table.command.aggregate.addToPot
import com.flexpoker.table.command.aggregate.fetchChipsWon
import com.flexpoker.table.command.aggregate.forcePlayerToShowCards
import com.flexpoker.table.command.aggregate.removePlayerFromAllPots
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePSet
import java.util.UUID

@UnitTestClass
class WinningPotTest {

    @Test
    fun testBasicTwoPersonPotHasSingleWinner() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation(
            playerId = player1,
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.EIGHT,
            firstKicker = CardRank.SEVEN,
            secondKicker = CardRank.FOUR,
            thirdKicker = CardRank.THREE,
            fourthKicker = CardRank.TWO,
        )
        val handEvaluation2 = HandEvaluation(
            playerId = player2,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), listOf(handEvaluation1, handEvaluation2),
            potId, setOf(player1, player2))
        pots = addToPot(pots,  potId, 60)

        val chipsWon = fetchChipsWon(pots, setOf(player1, player2))
        assertTrue(forcePlayerToShowCards(pots.first(), player1))
        assertFalse(forcePlayerToShowCards(pots.first(), player2))
        assertEquals(60, chipsWon[player1])
        assertEquals(0, chipsWon[player2])
    }

    @Test
    fun testThreePersonPotHasTwoWinners() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation(
            playerId = player1,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.EIGHT,
        )
        val handEvaluation2 = HandEvaluation(
            playerId = player2,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val handEvaluation3 = HandEvaluation(
            playerId = player3,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), listOf(handEvaluation1, handEvaluation2, handEvaluation3),
            potId, setOf(player1, player2, player3))
        pots = addToPot(pots, potId, 60)

        val chipsWon = fetchChipsWon(pots, setOf(player1, player2, player3))
        assertFalse(forcePlayerToShowCards(pots.first(), player1))
        assertTrue(forcePlayerToShowCards(pots.first(), player2))
        assertTrue(forcePlayerToShowCards(pots.first(), player3))
        assertEquals(0, chipsWon[player1])
        assertEquals(30, chipsWon[player2])
        assertEquals(30, chipsWon[player3])
    }

    @Test
    fun testThreePersonPotHasTwoWinnersWithBonus() {
        val player1 = UUID.fromString("a97f5601-422a-4232-8853-8a8c64b14fa1")
        val player2 = UUID.fromString("a97f5601-422a-4232-8853-8a8c64b14fa2")
        val player3 = UUID.fromString("a97f5601-422a-4232-8853-8a8c64b14fa3")
        val handEvaluation1 = HandEvaluation(
            playerId = player1,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.EIGHT,
        )
        val handEvaluation2 = HandEvaluation(
            playerId = player2,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val handEvaluation3 = HandEvaluation(
            playerId = player3,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val potId = UUID.fromString("8eb094f7-459f-4dba-b0d1-1ef3d48161ae")
        var pots = addNewPot(HashTreePSet.empty(), listOf(handEvaluation1, handEvaluation2, handEvaluation3),
            potId, setOf(player1, player2, player3))
        pots = addToPot(pots, potId, 61)

        val chipsWon = fetchChipsWon(pots, setOf(player1, player2, player3))
        assertFalse(forcePlayerToShowCards(pots.first(), player1))
        assertTrue(forcePlayerToShowCards(pots.first(), player2))
        assertTrue(forcePlayerToShowCards(pots.first(), player3))
        assertEquals(0, chipsWon[player1])
        assertEquals(31, chipsWon[player2])
        assertEquals(30, chipsWon[player3])
    }

    @Test
    fun testThreePersonPotHasTwoWinnersButOneOfTheWinnersFolds() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation(
            playerId = player1,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.EIGHT,
        )
        val handEvaluation2 = HandEvaluation(
            playerId = player2,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val handEvaluation3 = HandEvaluation(
            playerId = player3,
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.KING,
        )
        val potId = UUID.randomUUID()
        var pots = addNewPot(HashTreePSet.empty(), listOf(handEvaluation1, handEvaluation2, handEvaluation3),
            potId, setOf(player1, player2, player3))
        pots = addToPot(pots, potId, 60)
        pots = removePlayerFromAllPots(pots, player3)

        val chipsWon = fetchChipsWon(pots, setOf(player1, player2, player3))
        assertFalse(forcePlayerToShowCards(pots.first(), player1))
        assertTrue(forcePlayerToShowCards(pots.first(), player2))
        assertFalse(forcePlayerToShowCards(pots.first(), player3))
        assertEquals(0, chipsWon[player1])
        assertEquals(60, chipsWon[player2])
        assertEquals(0, chipsWon[player3])
    }

}