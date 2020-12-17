package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Arrays
import java.util.HashSet
import java.util.UUID

class BasicPotTest {

    @Test
    fun testPotIsOpenOnCreate() {
        val pot = createGenericPot()
        assertTrue(pot.isOpen)
    }

    @Test
    fun testPotClosesAfterClosing() {
        val pot = createGenericPot()
        pot.closePot()
        assertFalse(pot.isOpen)
    }

    @Test
    fun testPotCannotBeAddedToAfterClosing() {
        val pot = createGenericPot()
        pot.closePot()
        assertThrows(FlexPokerException::class.java) { pot.addChips(60) }
    }

    @Test
    fun testPotCannotHaveAPlayerRemovedAfterClosing() {
        val pot = createGenericPot()
        pot.closePot()
        assertThrows(FlexPokerException::class.java) { pot.removePlayer(UUID.randomUUID()) }
    }

    private fun createGenericPot(): Pot {
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val handEvaluation1 = HandEvaluation()
        handEvaluation1.playerId = player1
        handEvaluation1.handRanking = HandRanking.STRAIGHT
        handEvaluation1.primaryCardRank = CardRank.EIGHT
        val handEvaluation2 = HandEvaluation()
        handEvaluation2.playerId = player2
        handEvaluation2.handRanking = HandRanking.STRAIGHT
        handEvaluation2.primaryCardRank = CardRank.KING
        return Pot(UUID.randomUUID(), HashSet(Arrays.asList(handEvaluation1, handEvaluation2)))
    }

}