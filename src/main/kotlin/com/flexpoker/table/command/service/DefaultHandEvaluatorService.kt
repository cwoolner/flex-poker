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

data class CommonCards(val flopCards: FlopCards, val turnCard: TurnCard, val riverCard: RiverCard) {
    val cards: List<Card>
        get() = listOf(flopCards.card1, flopCards.card2, flopCards.card3, turnCard.card, riverCard.card)
}

@Service
class DefaultHandEvaluatorService : HandEvaluatorService {

    override fun determinePossibleHands(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard): List<HandRanking> {
        val commonCards = CommonCards(flopCards, turnCard, riverCard)
        val possibleHandRankings = ArrayList(Arrays.asList(*HandRanking.values()))
        filterByStraightFlushStatus(commonCards, possibleHandRankings)
        filterByFourOfAKindStatus(commonCards, possibleHandRankings)
        filterByFullHouseStatus(commonCards, possibleHandRankings)
        filterByFlushStatus(commonCards, possibleHandRankings)
        filterByStraightStatus(commonCards, possibleHandRankings)
        filterByThreeOfAKindStatus(commonCards, possibleHandRankings)
        filterByTwoPairStatus(commonCards, possibleHandRankings)
        filterByOnePairStatus(commonCards, possibleHandRankings)
        return possibleHandRankings
    }

    override fun determineHandEvaluation(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard,
                                         pocketCards: List<PocketCards>): Map<PocketCards, HandEvaluation> {
        val commonCards = CommonCards(flopCards, turnCard, riverCard)
        return pocketCards.associateWith {
            val cardList = commonCards.cards.plus(it.card1).plus(it.card2)
            val handEvaluation = HandEvaluation()
            fillInHandEvaluation(handEvaluation, cardList)
            handEvaluation
        }
    }

    /**
     * Enum meant to be used as a return value for the hand ranking methods. The
     * returned value is then used to decide what hand rankings are available.
     */
    private enum class CommonCardStatus {
        NOT_POSSIBLE, POSSIBLE, BOARD
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

    private fun filterByStraightFlushStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        val straightFlushStatus = determineStraightFlushStatus(commonCards)
        if (straightFlushStatus == CommonCardStatus.BOARD) {
            possibleHandRankings.remove(HandRanking.FOUR_OF_A_KIND)
            possibleHandRankings.remove(HandRanking.FULL_HOUSE)
            possibleHandRankings.remove(HandRanking.FLUSH)
            possibleHandRankings.remove(HandRanking.STRAIGHT)
            possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND)
            possibleHandRankings.remove(HandRanking.TWO_PAIR)
            possibleHandRankings.remove(HandRanking.ONE_PAIR)
            possibleHandRankings.remove(HandRanking.HIGH_CARD)
        } else if (straightFlushStatus == CommonCardStatus.NOT_POSSIBLE) {
            possibleHandRankings.remove(HandRanking.STRAIGHT_FLUSH)
        }
    }

    private fun filterByFourOfAKindStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.FOUR_OF_A_KIND)) {
            val fourOfAKindStatus = determineFourOfAKindStatus(commonCards)
            if (fourOfAKindStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.STRAIGHT_FLUSH)
                possibleHandRankings.remove(HandRanking.FULL_HOUSE)
                possibleHandRankings.remove(HandRanking.FLUSH)
                possibleHandRankings.remove(HandRanking.STRAIGHT)
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND)
                possibleHandRankings.remove(HandRanking.TWO_PAIR)
                possibleHandRankings.remove(HandRanking.ONE_PAIR)
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            } else if (fourOfAKindStatus == CommonCardStatus.NOT_POSSIBLE) {
                possibleHandRankings.remove(HandRanking.FOUR_OF_A_KIND)
                possibleHandRankings.remove(HandRanking.FULL_HOUSE)
            }
        }
    }

    private fun filterByFullHouseStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.FULL_HOUSE)) {
            val fullHouseStatus = determineFullHouseStatus(commonCards)
            if (fullHouseStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.FLUSH)
                possibleHandRankings.remove(HandRanking.STRAIGHT)
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND)
                possibleHandRankings.remove(HandRanking.TWO_PAIR)
                possibleHandRankings.remove(HandRanking.ONE_PAIR)
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            }
        }
    }

    private fun filterByFlushStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.FLUSH)) {
            val flushStatus = determineFlushStatus(commonCards)
            if (flushStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.STRAIGHT)
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND)
                possibleHandRankings.remove(HandRanking.TWO_PAIR)
                possibleHandRankings.remove(HandRanking.ONE_PAIR)
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            } else if (flushStatus == CommonCardStatus.NOT_POSSIBLE) {
                possibleHandRankings.remove(HandRanking.FLUSH)
            }
        }
    }

    private fun filterByStraightStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.STRAIGHT)) {
            val straightStatus = determineStraightStatus(commonCards)
            if (straightStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND)
                possibleHandRankings.remove(HandRanking.TWO_PAIR)
                possibleHandRankings.remove(HandRanking.ONE_PAIR)
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            } else if (straightStatus == CommonCardStatus.NOT_POSSIBLE) {
                possibleHandRankings.remove(HandRanking.STRAIGHT)
            }
        }
    }

    private fun filterByThreeOfAKindStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.THREE_OF_A_KIND)) {
            val threeOfAKindStatus = determineThreeOfAKindStatus(commonCards)
            if (threeOfAKindStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.TWO_PAIR)
                possibleHandRankings.remove(HandRanking.ONE_PAIR)
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            }
        }
    }

    private fun filterByTwoPairStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.TWO_PAIR)) {
            val twoPairStatus = determineTwoPairStatus(commonCards)
            if (twoPairStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND)
                possibleHandRankings.remove(HandRanking.ONE_PAIR)
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            }
        }
    }

    private fun filterByOnePairStatus(commonCards: CommonCards, possibleHandRankings: MutableList<HandRanking>) {
        if (possibleHandRankings.contains(HandRanking.ONE_PAIR)) {
            val onePairStatus = determineOnePairStatus(commonCards)
            if (onePairStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.HIGH_CARD)
            }
        }
    }

    private fun determineStraightFlushStatus(commonCards: CommonCards): CommonCardStatus {
        val cards = findCardsInLargestSuit(commonCards.cards)

        // we have a flush, check to see if it's a straight also
        if (cards!!.size == 5 && isStraight(commonCards)) {
            return CommonCardStatus.BOARD
        }
        if (cards.size < 3) {
            return CommonCardStatus.NOT_POSSIBLE
        }
        return if (isStraightPossible(cards)) {
            CommonCardStatus.POSSIBLE
        } else CommonCardStatus.NOT_POSSIBLE
    }

    private fun determineFourOfAKindStatus(commonCards: CommonCards): CommonCardStatus {
        val sortedCardList = commonCards.cards.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        if (cardRank1 === cardRank2 && cardRank2 === cardRank3 && cardRank3 === cardRank4
            || cardRank2 === cardRank3 && cardRank3 === cardRank4 && cardRank4 === cardRank5
        ) {
            return CommonCardStatus.BOARD
        }
        return if (cardRank1 === cardRank2 || cardRank2 === cardRank3 || cardRank3 === cardRank4 || cardRank4 === cardRank5) {
            CommonCardStatus.POSSIBLE
        } else CommonCardStatus.NOT_POSSIBLE
    }

    private fun determineFullHouseStatus(commonCards: CommonCards): CommonCardStatus {
        val sortedCardList = commonCards.cards.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        if (cardRank1 === cardRank2 && cardRank2 === cardRank3 && cardRank4 === cardRank5
            || cardRank1 === cardRank2 && cardRank3 === cardRank4 && cardRank4 === cardRank5
        ) {
            return CommonCardStatus.BOARD
        }
        return if (cardRank1 === cardRank2 || cardRank2 === cardRank3 || cardRank3 === cardRank4 || cardRank4 === cardRank5) {
            CommonCardStatus.POSSIBLE
        } else CommonCardStatus.NOT_POSSIBLE
    }

    private fun determineFlushStatus(commonCards: CommonCards): CommonCardStatus {
        val cards = findCardsInLargestSuit(commonCards.cards)
        return when (cards!!.size) {
            5 -> CommonCardStatus.BOARD
            4 -> CommonCardStatus.POSSIBLE
            3 -> CommonCardStatus.POSSIBLE
            else -> CommonCardStatus.NOT_POSSIBLE
        }
    }

    private fun determineStraightStatus(commonCards: CommonCards): CommonCardStatus {
        if (isStraight(commonCards)) {
            return CommonCardStatus.BOARD
        }
        return if (isStraightPossible(commonCards.cards)) {
            CommonCardStatus.POSSIBLE
        } else CommonCardStatus.NOT_POSSIBLE
    }

    private fun determineThreeOfAKindStatus(commonCards: CommonCards): CommonCardStatus {
        val sortedCardList = commonCards.cards.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        return if (cardRank1 === cardRank2 && cardRank2 === cardRank3
            || cardRank2 === cardRank3 && cardRank3 === cardRank4
            || cardRank3 === cardRank4 && cardRank4 === cardRank5
        ) {
            CommonCardStatus.BOARD
        } else CommonCardStatus.POSSIBLE
    }

    private fun determineTwoPairStatus(commonCards: CommonCards): CommonCardStatus {
        val sortedCardList = commonCards.cards.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        return if (cardRank1 === cardRank2 && cardRank3 === cardRank4
            || cardRank1 === cardRank2 && cardRank4 === cardRank5
            || cardRank2 === cardRank3 && cardRank4 === cardRank5
        ) {
            CommonCardStatus.BOARD
        } else CommonCardStatus.POSSIBLE
    }

    private fun determineOnePairStatus(commonCards: CommonCards): CommonCardStatus {
        val sortedCardList = commonCards.cards.sorted()
        val cardRank1 = sortedCardList[0].cardRank
        val cardRank2 = sortedCardList[1].cardRank
        val cardRank3 = sortedCardList[2].cardRank
        val cardRank4 = sortedCardList[3].cardRank
        val cardRank5 = sortedCardList[4].cardRank
        return if (cardRank1 === cardRank2 || cardRank2 === cardRank3 || cardRank3 === cardRank4 || cardRank4 === cardRank5) {
            CommonCardStatus.BOARD
        } else CommonCardStatus.POSSIBLE
    }

    private fun isStraight(commonCards: CommonCards): Boolean {
        val sortedCardList = commonCards.cards.sorted()
        val cardRankOrdinal1 = sortedCardList[0].cardRank.ordinal
        val cardRankOrdinal2 = sortedCardList[1].cardRank.ordinal
        val cardRankOrdinal3 = sortedCardList[2].cardRank.ordinal
        val cardRankOrdinal4 = sortedCardList[3].cardRank.ordinal
        val cardRankOrdinal5 = sortedCardList[4].cardRank.ordinal
        if (cardRankOrdinal1 + 1 != cardRankOrdinal2 || cardRankOrdinal2 + 1 != cardRankOrdinal3 || cardRankOrdinal3 + 1 != cardRankOrdinal4) {
            return false
        }

        // check to see if it's a 2, then see if the 5th card is an ace
        return if (cardRankOrdinal1 == 0 && cardRankOrdinal5 == 12) {
            true
        } else cardRankOrdinal4 + 1 == cardRankOrdinal5
    }

    private fun isStraightPossible(cardList: List<Card>?): Boolean {
        val cardRanks = ArrayList<CardRank>()
        for ((_, cardRank) in cardList!!) {
            cardRanks.add(cardRank)
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.ACE, CardRank.TWO,
                        CardRank.THREE, CardRank.FOUR, CardRank.FIVE
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.TWO, CardRank.THREE,
                        CardRank.FOUR, CardRank.FIVE, CardRank.SIX
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.THREE, CardRank.FOUR,
                        CardRank.FIVE, CardRank.SIX, CardRank.SEVEN
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.FOUR, CardRank.FIVE,
                        CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.FIVE, CardRank.SIX,
                        CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.SIX, CardRank.SEVEN,
                        CardRank.EIGHT, CardRank.NINE, CardRank.TEN
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.SEVEN, CardRank.EIGHT,
                        CardRank.NINE, CardRank.TEN, CardRank.JACK
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.EIGHT, CardRank.NINE,
                        CardRank.TEN, CardRank.JACK, CardRank.QUEEN
                    )
                ), 3
            )
        ) {
            return true
        }
        if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.NINE, CardRank.TEN,
                        CardRank.JACK, CardRank.QUEEN, CardRank.KING
                    )
                ), 3
            )
        ) {
            return true
        }
        return if (doCardRanksMatch(
                cardRanks,
                Arrays.asList(
                    *arrayOf(
                        CardRank.TEN, CardRank.JACK,
                        CardRank.QUEEN, CardRank.KING, CardRank.ACE
                    )
                ), 3
            )
        ) {
            true
        } else false
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