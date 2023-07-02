package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation

fun evaluateStraightFlush(cardList: List<Card>): HandEvaluation? {
    val cardsInLargestSuit = findCardsInLargestSuit(cardList)
    if (cardsInLargestSuit.size < 5) {
        return null
    }
    val cardRank = findCardRankOfHighestStraight(cardsInLargestSuit) ?: return null
    return HandEvaluation(
        handRanking = HandRanking.STRAIGHT_FLUSH,
        primaryCardRank = cardRank,
    )
}

fun evaluateFourOfAKind(cardList: List<Card>): HandEvaluation? {
    val sortedCardList = cardList.sorted()
    val cardRank1 = sortedCardList[0].cardRank
    val cardRank2 = sortedCardList[1].cardRank
    val cardRank3 = sortedCardList[2].cardRank
    val cardRank4 = sortedCardList[3].cardRank
    val cardRank5 = sortedCardList[4].cardRank
    val cardRank6 = sortedCardList[5].cardRank
    val cardRank7 = sortedCardList[6].cardRank
    if (cardRank1 === cardRank4) {
        return HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = cardRank1,
            firstKicker = cardRank7,
        )
    } else if (cardRank2 === cardRank5) {
        return HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = cardRank2,
            firstKicker = cardRank7,
        )
    } else if (cardRank3 === cardRank6) {
        return HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = cardRank3,
            firstKicker = cardRank7,
        )
    } else if (cardRank4 === cardRank7) {
        return HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = cardRank4,
            firstKicker = cardRank3,
        )
    } else {
        return null
    }
}

fun evaluateFullHouse(cardList: List<Card>): HandEvaluation? {
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
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank5,
                secondaryCardRank = cardRank3,
            )
        } else if (cardRank2 === cardRank3) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank5,
                secondaryCardRank = cardRank2,
            )
        } else if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank5,
                secondaryCardRank = cardRank1
            )
        }
    } else if (cardRank4 === cardRank6) {
        if (cardRank2 === cardRank3) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank4,
                secondaryCardRank = cardRank2,
            )
        } else if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank4,
                secondaryCardRank = cardRank1,
            )
        }
    } else if (cardRank3 === cardRank5) {
        if (cardRank6 === cardRank7) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank3,
                secondaryCardRank = cardRank6,
            )
        } else if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank3,
                secondaryCardRank = cardRank1,
            )
        }
    } else if (cardRank2 === cardRank4) {
        if (cardRank6 === cardRank7) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank2,
                secondaryCardRank = cardRank6,
            )
        } else if (cardRank5 === cardRank6) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank2,
                secondaryCardRank = cardRank5,
            )
        }
    } else if (cardRank1 === cardRank3) {
        if (cardRank6 === cardRank7) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank1,
                secondaryCardRank = cardRank6,
            )
        } else if (cardRank5 === cardRank6) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank1,
                secondaryCardRank = cardRank5,
            )
        } else if (cardRank4 === cardRank5) {
            return HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRank1,
                secondaryCardRank = cardRank4,
            )
        }
    }
    return null
}

fun evaluateFlush(cardList: List<Card>): HandEvaluation? {
    val cards = findCardsInLargestSuit(cardList)
    if (cards.size < 5) {
        return null
    }
    val sortedCardList = cards.sorted()
    return HandEvaluation(
        handRanking = HandRanking.FLUSH,
        primaryCardRank = sortedCardList[cards.size - 1].cardRank,
        firstKicker = sortedCardList[cards.size - 2].cardRank,
        secondKicker = sortedCardList[cards.size - 3].cardRank,
        thirdKicker = sortedCardList[cards.size - 4].cardRank,
        fourthKicker = sortedCardList[cards.size - 5].cardRank,
    )
}

fun evaluateStraight(cardList: List<Card>): HandEvaluation? {
    val cardRank = findCardRankOfHighestStraight(cardList) ?: return null
    return HandEvaluation(
        handRanking = HandRanking.STRAIGHT,
        primaryCardRank = cardRank,
    )
}

fun evaluateThreeOfAKind(cardList: List<Card>): HandEvaluation? {
    val sortedCardList = cardList.sorted()
    val cardRank1 = sortedCardList[0].cardRank
    val cardRank2 = sortedCardList[1].cardRank
    val cardRank3 = sortedCardList[2].cardRank
    val cardRank4 = sortedCardList[3].cardRank
    val cardRank5 = sortedCardList[4].cardRank
    val cardRank6 = sortedCardList[5].cardRank
    val cardRank7 = sortedCardList[6].cardRank
    if (cardRank5 === cardRank7) {
        return HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = cardRank5,
            firstKicker = cardRank4,
            secondKicker = cardRank3,
        )
    } else if (cardRank4 === cardRank6) {
        return HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = cardRank4,
            firstKicker = cardRank7,
            secondKicker = cardRank3,
        )
    } else if (cardRank3 === cardRank5) {
        return HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = cardRank3,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
        )
    } else  if (cardRank2 === cardRank4) {
        return HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = cardRank2,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
        )
    } else if (cardRank1 === cardRank3) {
        return HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = cardRank1,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
        )
    } else {
        return null
    }
}

fun evaluateTwoPair(cardList: List<Card>): HandEvaluation? {
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
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank6,
                secondaryCardRank = cardRank4,
                firstKicker = cardRank3,
            )
        } else if (cardRank3 === cardRank4) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank6,
                secondaryCardRank = cardRank3,
                firstKicker = cardRank5,
            )
        } else if (cardRank2 === cardRank3) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank6,
                secondaryCardRank = cardRank2,
                firstKicker = cardRank5,
            )
        } else if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank6,
                secondaryCardRank = cardRank1,
                firstKicker = cardRank5,
            )
        }
    } else if (cardRank5 === cardRank6) {
        if (cardRank3 === cardRank4) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank5,
                secondaryCardRank = cardRank3,
                firstKicker = cardRank7,
            )
        } else if (cardRank2 === cardRank3) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank5,
                secondaryCardRank = cardRank2,
                firstKicker = cardRank7,
            )
        } else if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank5,
                secondaryCardRank = cardRank1,
                firstKicker = cardRank7,
            )
        }
    } else if (cardRank4 === cardRank5) {
        if (cardRank2 === cardRank3) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank4,
                secondaryCardRank = cardRank2,
                firstKicker = cardRank7,
            )
        } else if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank4,
                secondaryCardRank = cardRank1,
                firstKicker = cardRank7,
            )
        }
    } else if (cardRank3 === cardRank4) {
        if (cardRank1 === cardRank2) {
            return HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRank4,
                secondaryCardRank = cardRank1,
                firstKicker = cardRank7,
            )
        }
    }
    return null
}

fun evaluateOnePair(cardList: List<Card>): HandEvaluation? {
    val sortedCardList = cardList.sorted()
    val cardRank1 = sortedCardList[0].cardRank
    val cardRank2 = sortedCardList[1].cardRank
    val cardRank3 = sortedCardList[2].cardRank
    val cardRank4 = sortedCardList[3].cardRank
    val cardRank5 = sortedCardList[4].cardRank
    val cardRank6 = sortedCardList[5].cardRank
    val cardRank7 = sortedCardList[6].cardRank
    if (cardRank6 === cardRank7) {
        return HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRank6,
            firstKicker = cardRank5,
            secondKicker = cardRank4,
            thirdKicker = cardRank3,
        )
    } else if (cardRank5 === cardRank6) {
        return HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRank5,
            firstKicker = cardRank7,
            secondKicker = cardRank4,
            thirdKicker = cardRank3,
        )
    } else if (cardRank4 === cardRank5) {
        return HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRank4,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
            thirdKicker = cardRank3,
        )
    } else if (cardRank3 === cardRank4) {
        return HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRank3,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
            thirdKicker = cardRank5,
        )
    } else if (cardRank2 === cardRank3) {
        return HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRank2,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
            thirdKicker = cardRank5,
        )
    } else if (cardRank1 === cardRank2) {
        return HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRank1,
            firstKicker = cardRank7,
            secondKicker = cardRank6,
            thirdKicker = cardRank5,
        )
    } else {
        return null
    }
}

fun evaluateHighCard(cardList: List<Card>): HandEvaluation {
    val sortedCardList = cardList.sorted()
    return HandEvaluation(
        handRanking = HandRanking.HIGH_CARD,
        primaryCardRank = sortedCardList[6].cardRank,
        firstKicker = sortedCardList[5].cardRank,
        secondKicker = sortedCardList[4].cardRank,
        thirdKicker = sortedCardList[3].cardRank,
        fourthKicker = sortedCardList[2].cardRank,
    )
}

private fun findCardsInLargestSuit(cards: List<Card>): List<Card> {
    val suitMap = mapOf<CardSuit, MutableList<Card>>(
        CardSuit.CLUBS to mutableListOf(),
        CardSuit.DIAMONDS to mutableListOf(),
        CardSuit.HEARTS to mutableListOf(),
        CardSuit.SPADES to mutableListOf(),
    )
    cards.forEach {
        suitMap[it.cardSuit]!!.add(it)
    }
    return suitMap.values.maxBy { it.size }
}

private fun findCardRankOfHighestStraight(cardList: List<Card>): CardRank? {
    val cardRanks = cardList.map { it.cardRank }
    if (cardRanks.containsAll(listOf(CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE))) {
        return CardRank.ACE
    } else if (cardRanks.containsAll(listOf(CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING))) {
        return CardRank.KING
    } else if (cardRanks.containsAll(listOf(CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN))) {
        return CardRank.QUEEN
    } else if (cardRanks.containsAll(listOf(CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK))) {
        return CardRank.JACK
    } else if (cardRanks.containsAll(listOf(CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN))) {
        return CardRank.TEN
    } else if (cardRanks.containsAll(listOf(CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE))) {
        return CardRank.NINE
    } else if (cardRanks.containsAll(listOf(CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT))) {
        return CardRank.EIGHT
    } else if (cardRanks.containsAll(listOf(CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN))) {
        return CardRank.SEVEN
    } else if (cardRanks.containsAll(listOf(CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX))) {
        return CardRank.SIX
    } else if (cardRanks.containsAll(listOf(CardRank.ACE, CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE))) {
        return CardRank.FIVE
    } else {
        return null
    }
}
