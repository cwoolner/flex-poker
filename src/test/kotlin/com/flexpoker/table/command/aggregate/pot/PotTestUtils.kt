package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import java.util.UUID

fun createPotHand(playerId: UUID): HandEvaluation {
    val handEvaluation1 = HandEvaluation()
    handEvaluation1.playerId = playerId
    handEvaluation1.handRanking = HandRanking.FLUSH
    handEvaluation1.primaryCardRank = CardRank.EIGHT
    handEvaluation1.firstKicker = CardRank.SEVEN
    handEvaluation1.secondKicker = CardRank.FOUR
    handEvaluation1.thirdKicker = CardRank.THREE
    handEvaluation1.fourthKicker = CardRank.TWO
    return handEvaluation1
}

fun createPotHands(player1: UUID, player2: UUID): List<HandEvaluation> {
    val handEvaluation1 = createPotHand(player1)
    val handEvaluation2 = HandEvaluation()
    handEvaluation2.playerId = player2
    handEvaluation2.handRanking = HandRanking.STRAIGHT
    handEvaluation2.primaryCardRank = CardRank.KING
    return listOf(handEvaluation1, handEvaluation2)
}