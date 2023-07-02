package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.HandEvaluation
import org.springframework.stereotype.Service

@Service
class DefaultHandEvaluatorService : HandEvaluatorService {

    override fun determineHandEvaluation(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard,
                                         pocketCards: List<PocketCards>): Map<PocketCards, HandEvaluation> {
        val commonCards = listOf(flopCards.card1, flopCards.card2, flopCards.card3, turnCard.card, riverCard.card)
        return pocketCards.associateWith {
            val cardList = commonCards.plus(it.card1).plus(it.card2)
            fillInHandEvaluation(cardList)
        }
    }

    private fun fillInHandEvaluation(cardList: List<Card>): HandEvaluation {
        return HandRanking.values().reversed().firstNotNullOf {
            when (it) {
                HandRanking.STRAIGHT_FLUSH -> evaluateStraightFlush(cardList)
                HandRanking.FOUR_OF_A_KIND -> evaluateFourOfAKind(cardList)
                HandRanking.FULL_HOUSE -> evaluateFullHouse(cardList)
                HandRanking.FLUSH -> evaluateFlush(cardList)
                HandRanking.STRAIGHT -> evaluateStraight(cardList)
                HandRanking.THREE_OF_A_KIND -> evaluateThreeOfAKind(cardList)
                HandRanking.TWO_PAIR -> evaluateTwoPair(cardList)
                HandRanking.ONE_PAIR -> evaluateOnePair(cardList)
                HandRanking.HIGH_CARD -> evaluateHighCard(cardList)
            }
        }
    }

}