package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.HandEvaluation
import org.springframework.stereotype.Service
import java.util.Arrays

@Service
class DefaultHandEvaluatorService : HandEvaluatorService {

    override fun determineHandEvaluation(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard,
                                         pocketCards: List<PocketCards>): Map<PocketCards, HandEvaluation> {
        val commonCards = listOf(flopCards.card1, flopCards.card2, flopCards.card3, turnCard.card, riverCard.card)
        return pocketCards.associateWith {
            val cardList = commonCards.plus(it.card1).plus(it.card2)
            val handEvaluation = HandEvaluation()
            fillInHandEvaluation(handEvaluation, cardList)
            handEvaluation
        }
    }

    private fun fillInHandEvaluation(handEvaluation: HandEvaluation, cardList: List<Card>) {
        HandRanking.values().reversed().forEach {
            when (it) {
                HandRanking.STRAIGHT_FLUSH -> if (evaluateStraightFlush(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.STRAIGHT_FLUSH
                    return
                }
                HandRanking.FOUR_OF_A_KIND -> if (evaluateFourOfAKind(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.FOUR_OF_A_KIND
                    return
                }
                HandRanking.FULL_HOUSE -> if (evaluateFullHouse(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.FULL_HOUSE
                    return
                }
                HandRanking.FLUSH -> if (evaluateFlush(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.FLUSH
                    return
                }
                HandRanking.STRAIGHT -> if (evaluateStraight(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.STRAIGHT
                    return
                }
                HandRanking.THREE_OF_A_KIND -> if (evaluateThreeOfAKind(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.THREE_OF_A_KIND
                    return
                }
                HandRanking.TWO_PAIR -> if (evaluateTwoPair(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.TWO_PAIR
                    return
                }
                HandRanking.ONE_PAIR -> if (evaluateOnePair(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.ONE_PAIR
                    return
                }
                HandRanking.HIGH_CARD -> if (evaluateHighCard(handEvaluation, cardList)) {
                    handEvaluation.handRanking = HandRanking.HIGH_CARD
                    return
                }
            }
        }
    }

    private fun evaluateStraightFlush(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val cardsInLargestSuit = findCardsInLargestSuit(cardList)
        if (cardsInLargestSuit!!.size < 5) {
            return false
        }
        val cardRank = findCardRankOfHighestStraight(cardsInLargestSuit) ?: return false
        handEvaluation.primaryCardRank = cardRank
        return true
    }

    private fun evaluateFourOfAKind(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val sortedCardList = cardList.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        val cardRank6 = sortedCardList[5].cardRank
        val cardRank7 = sortedCardList[6].cardRank
        if (cardRank1 === cardRank4) {
            handEvaluation.primaryCardRank = cardRank1
            handEvaluation.firstKicker = cardRank7
            return true
        }
        if (cardRank2 === cardRank5) {
            handEvaluation.primaryCardRank = cardRank2
            handEvaluation.firstKicker = cardRank7
            return true
        }
        if (cardRank3 === cardRank6) {
            handEvaluation.primaryCardRank = cardRank3
            handEvaluation.firstKicker = cardRank7
            return true
        }
        if (cardRank4 === cardRank7) {
            handEvaluation.primaryCardRank = cardRank4
            handEvaluation.firstKicker = cardRank3
            return true
        }
        return false
    }

    private fun evaluateFullHouse(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val sortedCardList = cardList.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        val cardRank6 = sortedCardList[5].cardRank
        val cardRank7 = sortedCardList[6].cardRank
        if (cardRank5 === cardRank7) {
            if (cardRank3 === cardRank4) {
                handEvaluation.primaryCardRank = cardRank5
                handEvaluation.secondaryCardRank = cardRank3
                return true
            }
            if (cardRank2 === cardRank3) {
                handEvaluation.primaryCardRank = cardRank5
                handEvaluation.secondaryCardRank = cardRank2
                return true
            }
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank5
                handEvaluation.secondaryCardRank = cardRank1
                return true
            }
        }
        if (cardRank4 === cardRank6) {
            if (cardRank2 === cardRank3) {
                handEvaluation.primaryCardRank = cardRank4
                handEvaluation.secondaryCardRank = cardRank2
                return true
            }
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank4
                handEvaluation.secondaryCardRank = cardRank1
                return true
            }
        }
        if (cardRank3 === cardRank5) {
            if (cardRank6 === cardRank7) {
                handEvaluation.primaryCardRank = cardRank3
                handEvaluation.secondaryCardRank = cardRank6
                return true
            }
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank3
                handEvaluation.secondaryCardRank = cardRank1
                return true
            }
        }
        if (cardRank2 === cardRank4) {
            if (cardRank6 === cardRank7) {
                handEvaluation.primaryCardRank = cardRank2
                handEvaluation.secondaryCardRank = cardRank6
                return true
            }
            if (cardRank5 === cardRank6) {
                handEvaluation.primaryCardRank = cardRank2
                handEvaluation.secondaryCardRank = cardRank5
                return true
            }
        }
        if (cardRank1 === cardRank3) {
            if (cardRank6 === cardRank7) {
                handEvaluation.primaryCardRank = cardRank1
                handEvaluation.secondaryCardRank = cardRank6
                return true
            }
            if (cardRank5 === cardRank6) {
                handEvaluation.primaryCardRank = cardRank1
                handEvaluation.secondaryCardRank = cardRank5
                return true
            }
            if (cardRank4 === cardRank5) {
                handEvaluation.primaryCardRank = cardRank1
                handEvaluation.secondaryCardRank = cardRank4
                return true
            }
        }
        return false
    }

    private fun evaluateFlush(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val cards = findCardsInLargestSuit(cardList)
        if (cards!!.size < 5) {
            return false
        }
        val sortedCardList = cards.sorted()
        handEvaluation.primaryCardRank = sortedCardList[cards.size - 1].cardRank
        handEvaluation.firstKicker = sortedCardList[cards.size - 2].cardRank
        handEvaluation.secondKicker = sortedCardList[cards.size - 3].cardRank
        handEvaluation.thirdKicker = sortedCardList[cards.size - 4].cardRank
        handEvaluation.fourthKicker = sortedCardList[cards.size - 5].cardRank
        return true
    }

    private fun evaluateStraight(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val cardRank = findCardRankOfHighestStraight(cardList) ?: return false
        handEvaluation.primaryCardRank = cardRank
        return true
    }

    private fun evaluateThreeOfAKind(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val sortedCardList = cardList.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        val cardRank6 = sortedCardList[5].cardRank
        val cardRank7 = sortedCardList[6].cardRank
        if (cardRank5 === cardRank7) {
            handEvaluation.primaryCardRank = cardRank5
            handEvaluation.firstKicker = cardRank4
            handEvaluation.secondKicker = cardRank3
            return true
        }
        if (cardRank4 === cardRank6) {
            handEvaluation.primaryCardRank = cardRank4
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank3
            return true
        }
        if (cardRank3 === cardRank5) {
            handEvaluation.primaryCardRank = cardRank3
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            return true
        }
        if (cardRank2 === cardRank4) {
            handEvaluation.primaryCardRank = cardRank2
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            return true
        }
        if (cardRank1 === cardRank3) {
            handEvaluation.primaryCardRank = cardRank1
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            return true
        }
        return false
    }

    private fun evaluateTwoPair(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val sortedCardList =  cardList.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        val cardRank6 = sortedCardList[5].cardRank
        val cardRank7 = sortedCardList[6].cardRank
        if (cardRank6 === cardRank7) {
            if (cardRank4 === cardRank5) {
                handEvaluation.primaryCardRank = cardRank6
                handEvaluation.secondaryCardRank = cardRank4
                handEvaluation.firstKicker = cardRank3
                return true
            }
            if (cardRank3 === cardRank4) {
                handEvaluation.primaryCardRank = cardRank6
                handEvaluation.secondaryCardRank = cardRank3
                handEvaluation.firstKicker = cardRank5
                return true
            }
            if (cardRank2 === cardRank3) {
                handEvaluation.primaryCardRank = cardRank6
                handEvaluation.secondaryCardRank = cardRank2
                handEvaluation.firstKicker = cardRank5
                return true
            }
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank6
                handEvaluation.secondaryCardRank = cardRank1
                handEvaluation.firstKicker = cardRank5
                return true
            }
        }
        if (cardRank5 === cardRank6) {
            if (cardRank3 === cardRank4) {
                handEvaluation.primaryCardRank = cardRank5
                handEvaluation.secondaryCardRank = cardRank3
                handEvaluation.firstKicker = cardRank7
                return true
            }
            if (cardRank2 === cardRank3) {
                handEvaluation.primaryCardRank = cardRank5
                handEvaluation.secondaryCardRank = cardRank2
                handEvaluation.firstKicker = cardRank7
                return true
            }
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank5
                handEvaluation.secondaryCardRank = cardRank1
                handEvaluation.firstKicker = cardRank7
                return true
            }
        }
        if (cardRank4 === cardRank5) {
            if (cardRank2 === cardRank3) {
                handEvaluation.primaryCardRank = cardRank4
                handEvaluation.secondaryCardRank = cardRank2
                handEvaluation.firstKicker = cardRank7
                return true
            }
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank4
                handEvaluation.secondaryCardRank = cardRank1
                handEvaluation.firstKicker = cardRank7
                return true
            }
        }
        if (cardRank3 === cardRank4) {
            if (cardRank1 === cardRank2) {
                handEvaluation.primaryCardRank = cardRank4
                handEvaluation.secondaryCardRank = cardRank1
                handEvaluation.firstKicker = cardRank7
                return true
            }
        }
        return false
    }

    private fun evaluateOnePair(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val sortedCardList = cardList.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        val cardRank6 = sortedCardList[5].cardRank
        val cardRank7 = sortedCardList[6].cardRank
        if (cardRank6 === cardRank7) {
            handEvaluation.primaryCardRank = cardRank6
            handEvaluation.firstKicker = cardRank5
            handEvaluation.secondKicker = cardRank4
            handEvaluation.thirdKicker = cardRank3
            return true
        }
        if (cardRank5 === cardRank6) {
            handEvaluation.primaryCardRank = cardRank5
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank4
            handEvaluation.thirdKicker = cardRank3
            return true
        }
        if (cardRank4 === cardRank5) {
            handEvaluation.primaryCardRank = cardRank4
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            handEvaluation.thirdKicker = cardRank3
            return true
        }
        if (cardRank3 === cardRank4) {
            handEvaluation.primaryCardRank = cardRank3
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            handEvaluation.thirdKicker = cardRank5
            return true
        }
        if (cardRank2 === cardRank3) {
            handEvaluation.primaryCardRank = cardRank2
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            handEvaluation.thirdKicker = cardRank5
            return true
        }
        if (cardRank1 === cardRank2) {
            handEvaluation.primaryCardRank = cardRank1
            handEvaluation.firstKicker = cardRank7
            handEvaluation.secondKicker = cardRank6
            handEvaluation.thirdKicker = cardRank5
            return true
        }
        return false
    }

    private fun evaluateHighCard(handEvaluation: HandEvaluation, cardList: List<Card>): Boolean {
        val sortedCardList = cardList.sorted()
        handEvaluation.primaryCardRank = sortedCardList[6].cardRank
        handEvaluation.firstKicker = sortedCardList[5].cardRank
        handEvaluation.secondKicker = sortedCardList[4].cardRank
        handEvaluation.thirdKicker = sortedCardList[3].cardRank
        handEvaluation.fourthKicker = sortedCardList[2].cardRank
        return true
    }

    private fun doCardRanksMatch(
        cardRanks: List<CardRank>,
        straightCardRanks: List<CardRank>,
        numberToMatch: Int
    ): Boolean {
        val cardRankSet = HashSet(cardRanks)
        var numberOfMatched = 0
        for (cardRank in cardRankSet) {
            if (straightCardRanks.contains(cardRank)) {
                numberOfMatched++
            }
        }
        return numberOfMatched >= numberToMatch
    }

    /**
     * Return the list of cards in the largest suit. If that number is three,
     * then the correct suit will be represented. If that number is only two,
     * then the first suit scanned will be returned in the following order:
     * HEARTS, CLUBS, DIAMONDS, SPADES
     */
    private fun findCardsInLargestSuit(cards: List<Card>): List<Card>? {
        val suitMap = HashMap<CardSuit, MutableList<Card>?>()
        for (card in cards) {
            val cardSuit = card.cardSuit
            if (suitMap[cardSuit] == null) {
                suitMap[cardSuit] = ArrayList()
            }
            suitMap[cardSuit]!!.add(card)
        }
        val numberOfHearts = if (suitMap[CardSuit.HEARTS] == null) 0 else suitMap[CardSuit.HEARTS]!!.size
        val numberOfClubs = if (suitMap[CardSuit.CLUBS] == null) 0 else suitMap[CardSuit.CLUBS]!!.size
        val numberOfDiamonds = if (suitMap[CardSuit.DIAMONDS] == null) 0 else suitMap[CardSuit.DIAMONDS]!!.size
        val numberOfSpades = if (suitMap[CardSuit.SPADES] == null) 0 else suitMap[CardSuit.SPADES]!!.size
        if (numberOfHearts >= 3) {
            return suitMap[CardSuit.HEARTS]
        }
        if (numberOfClubs >= 3) {
            return suitMap[CardSuit.CLUBS]
        }
        if (numberOfDiamonds >= 3) {
            return suitMap[CardSuit.DIAMONDS]
        }
        if (numberOfSpades >= 3) {
            return suitMap[CardSuit.SPADES]
        }
        if (numberOfHearts == 2) {
            return suitMap[CardSuit.HEARTS]
        }
        if (numberOfClubs == 2) {
            return suitMap[CardSuit.CLUBS]
        }
        if (numberOfDiamonds == 2) {
            return suitMap[CardSuit.DIAMONDS]
        }
        if (numberOfSpades == 2) {
            return suitMap[CardSuit.SPADES]
        }
        throw IllegalArgumentException("CommonCards must contain at least two instances of a suit.")
    }

    private fun findCardRankOfHighestStraight(cardList: List<Card>?): CardRank? {
        if (cardList!!.size < 5) {
            return null
        }
        val cardRanks = ArrayList<CardRank>()
        for ((_, cardRank) in cardList) {
            cardRanks.add(cardRank)
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.TEN, CardRank.JACK,
                        CardRank.QUEEN, CardRank.KING, CardRank.ACE
                    )
                ), 5
            )
        ) {
            return CardRank.ACE
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.NINE, CardRank.TEN,
                        CardRank.JACK, CardRank.QUEEN, CardRank.KING
                    )
                ), 5
            )
        ) {
            return CardRank.KING
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.EIGHT, CardRank.NINE,
                        CardRank.TEN, CardRank.JACK, CardRank.QUEEN
                    )
                ), 5
            )
        ) {
            return CardRank.QUEEN
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.SEVEN, CardRank.EIGHT,
                        CardRank.NINE, CardRank.TEN, CardRank.JACK
                    )
                ), 5
            )
        ) {
            return CardRank.JACK
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.SIX, CardRank.SEVEN,
                        CardRank.EIGHT, CardRank.NINE, CardRank.TEN
                    )
                ), 5
            )
        ) {
            return CardRank.TEN
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.FIVE, CardRank.SIX,
                        CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE
                    )
                ), 5
            )
        ) {
            return CardRank.NINE
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.FOUR, CardRank.FIVE,
                        CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT
                    )
                ), 5
            )
        ) {
            return CardRank.EIGHT
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.THREE, CardRank.FOUR,
                        CardRank.FIVE, CardRank.SIX, CardRank.SEVEN
                    )
                ), 5
            )
        ) {
            return CardRank.SEVEN
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.TWO, CardRank.THREE,
                        CardRank.FOUR, CardRank.FIVE, CardRank.SIX
                    )
                ), 5
            )
        ) {
            return CardRank.SIX
        }
        return if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.ACE, CardRank.TWO,
                        CardRank.THREE, CardRank.FOUR, CardRank.FIVE
                    )
                ), 5
            )
        ) {
            CardRank.FIVE
        } else null
    }
}