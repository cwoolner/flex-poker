package com.flexpoker.table.command.aggregate.pot

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import java.util.UUID

fun createPotHand(playerId: UUID): HandEvaluation {
    return HandEvaluation(
        playerId = playerId,
        handRanking = HandRanking.FLUSH,
        primaryCardRank = CardRank.EIGHT,
        firstKicker = CardRank.SEVEN,
        secondKicker = CardRank.FOUR,
        thirdKicker = CardRank.THREE,
        fourthKicker = CardRank.TWO,
    )
}

fun createPotHands(player1: UUID, player2: UUID): List<HandEvaluation> {
    val handEvaluation1 = createPotHand(player1)
    val handEvaluation2 = HandEvaluation(
        playerId = player2,
        handRanking = HandRanking.STRAIGHT,
        primaryCardRank = CardRank.KING,
    )
    return listOf(handEvaluation1, handEvaluation2)
}