package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet
import java.util.UUID

class WinningPotTest {

    @Test
    fun testBasicTwoPersonPotHasSingleWinner() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation()
        handEvaluation1.playerId = player1
        handEvaluation1.handRanking = HandRanking.FLUSH
        handEvaluation1.primaryCardRank = CardRank.EIGHT
        handEvaluation1.firstKicker = CardRank.SEVEN
        handEvaluation1.secondKicker = CardRank.FOUR
        handEvaluation1.thirdKicker = CardRank.THREE
        handEvaluation1.fourthKicker = CardRank.TWO
        val handEvaluation2 = HandEvaluation()
        handEvaluation2.playerId = player2
        handEvaluation2.handRanking = HandRanking.STRAIGHT
        handEvaluation2.primaryCardRank = CardRank.KING
        val winningHands = ArrayList<Any>()
        winningHands.add(handEvaluation1)
        winningHands.add(handEvaluation2)
        val pot = Pot(UUID.randomUUID(), HashSet(Arrays.asList(handEvaluation1, handEvaluation2)))
        pot.addChips(60)
        Assertions.assertTrue(pot.forcePlayerToShowCards(player1))
        Assertions.assertFalse(pot.forcePlayerToShowCards(player2))
        Assertions.assertEquals(60, pot.getChipsWon(player1))
        Assertions.assertEquals(0, pot.getChipsWon(player2))
    }

    @Test
    fun testThreePersonPotHasTwoWinners() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation()
        handEvaluation1.playerId = player1
        handEvaluation1.handRanking = HandRanking.STRAIGHT
        handEvaluation1.primaryCardRank = CardRank.EIGHT
        val handEvaluation2 = HandEvaluation()
        handEvaluation2.playerId = player2
        handEvaluation2.handRanking = HandRanking.STRAIGHT
        handEvaluation2.primaryCardRank = CardRank.KING
        val handEvaluation3 = HandEvaluation()
        handEvaluation3.playerId = player3
        handEvaluation3.handRanking = HandRanking.STRAIGHT
        handEvaluation3.primaryCardRank = CardRank.KING
        val pot = Pot(UUID.randomUUID(), mutableSetOf(handEvaluation1, handEvaluation2, handEvaluation3))
        pot.addChips(60)
        assertFalse(pot.forcePlayerToShowCards(player1))
        assertTrue(pot.forcePlayerToShowCards(player2))
        assertTrue(pot.forcePlayerToShowCards(player3))
        assertEquals(0, pot.getChipsWon(player1))
        assertEquals(30, pot.getChipsWon(player2))
        assertEquals(30, pot.getChipsWon(player3))
    }

    @Test
    fun testThreePersonPotHasTwoWinnersWithBonus() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation()
        handEvaluation1.playerId = player1
        handEvaluation1.handRanking = HandRanking.STRAIGHT
        handEvaluation1.primaryCardRank = CardRank.EIGHT
        val handEvaluation2 = HandEvaluation()
        handEvaluation2.playerId = player2
        handEvaluation2.handRanking = HandRanking.STRAIGHT
        handEvaluation2.primaryCardRank = CardRank.KING
        val handEvaluation3 = HandEvaluation()
        handEvaluation3.playerId = player3
        handEvaluation3.handRanking = HandRanking.STRAIGHT
        handEvaluation3.primaryCardRank = CardRank.KING
        val pot = Pot(UUID.randomUUID(), mutableSetOf(handEvaluation1, handEvaluation2, handEvaluation3))
        pot.addChips(61)

        assertFalse(pot.forcePlayerToShowCards(player1))
        assertTrue(pot.forcePlayerToShowCards(player2))
        assertTrue(pot.forcePlayerToShowCards(player3))
        assertEquals(0, pot.getChipsWon(player1))
        if (pot.getChipsWon(player2) == 30) {
            assertEquals(30, pot.getChipsWon(player2))
            assertEquals(31, pot.getChipsWon(player3))
        } else if (pot.getChipsWon(player3) == 30) {
            assertEquals(31, pot.getChipsWon(player2))
            assertEquals(30, pot.getChipsWon(player3))
        } else {
            throw IllegalStateException("one of the pots should be 30 and the other should be 31")
        }
    }

    @Test
    fun testThreePersonPotHasTwoWinnersButOneOfTheWinnersFolds() {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation()
        handEvaluation1.playerId = player1
        handEvaluation1.handRanking = HandRanking.STRAIGHT
        handEvaluation1.primaryCardRank = CardRank.EIGHT
        val handEvaluation2 = HandEvaluation()
        handEvaluation2.playerId = player2
        handEvaluation2.handRanking = HandRanking.STRAIGHT
        handEvaluation2.primaryCardRank = CardRank.KING
        val handEvaluation3 = HandEvaluation()
        handEvaluation3.playerId = player3
        handEvaluation3.handRanking = HandRanking.STRAIGHT
        handEvaluation3.primaryCardRank = CardRank.KING
        val pot = Pot(
            UUID.randomUUID(), HashSet(
                Arrays.asList(
                    handEvaluation1,
                    handEvaluation2, handEvaluation3
                )
            )
        )
        pot.addChips(60)
        pot.removePlayer(player3)

        assertFalse(pot.forcePlayerToShowCards(player1))
        assertTrue(pot.forcePlayerToShowCards(player2))
        assertFalse(pot.forcePlayerToShowCards(player3))
        assertEquals(0, pot.getChipsWon(player1))
        assertEquals(60, pot.getChipsWon(player2))
        assertEquals(0, pot.getChipsWon(player3))
    }

}