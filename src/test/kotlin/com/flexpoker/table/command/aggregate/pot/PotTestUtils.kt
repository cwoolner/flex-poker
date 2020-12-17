package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import java.util.ArrayList
import java.util.UUID

object PotTestUtils {

    fun createBasicPotHandler(player1: UUID?, player2: UUID?): PotHandler {
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
        val winningHands = ArrayList<HandEvaluation>()
        winningHands.add(handEvaluation1)
        winningHands.add(handEvaluation2)
        return PotHandler(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), winningHands)
    }

}