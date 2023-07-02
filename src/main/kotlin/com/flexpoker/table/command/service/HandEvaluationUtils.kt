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
    val cardRanks = cardList.map { it.cardRank }.sorted()
    val fourOfAKindIndex = (3 downTo 0).firstNotNullOfOrNull { getMatchingIndex(cardRanks, it, 4) }
    return if (fourOfAKindIndex != null) {
        val cardRanksMinusFourOfAKind = removeCardRanksStartingAtIndex(cardRanks, fourOfAKindIndex, 4)
        HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = cardRanks[fourOfAKindIndex],
            firstKicker = cardRanksMinusFourOfAKind[2],
        )
    } else null
}

fun evaluateFullHouse(cardList: List<Card>): HandEvaluation? {
    val cardRanks =  cardList.map { it.cardRank }.sorted()
    val matchingThreeOfAKindIndex = (4 downTo 0).firstNotNullOfOrNull { getMatchingIndex(cardRanks, it, 3) }
    return if (matchingThreeOfAKindIndex != null) {
        val cardRanksMinusThreeOfAKind = removeCardRanksStartingAtIndex(cardRanks, matchingThreeOfAKindIndex, 3)
        val matchingPairIndex = (2 downTo 0).firstNotNullOfOrNull { getMatchingIndex(cardRanksMinusThreeOfAKind, it, 2) }
        if (matchingPairIndex != null) {
            HandEvaluation(
                handRanking = HandRanking.FULL_HOUSE,
                primaryCardRank = cardRanks[matchingThreeOfAKindIndex],
                secondaryCardRank = cardRanksMinusThreeOfAKind[matchingPairIndex],
            )
        } else null
    } else null
}

fun evaluateFlush(cardList: List<Card>): HandEvaluation? {
    val cards = findCardsInLargestSuit(cardList)
    if (cards.size < 5) {
        return null
    }
    val flushCardRanks = cards.map { it.cardRank }.sorted()
    return HandEvaluation(
        handRanking = HandRanking.FLUSH,
        primaryCardRank = flushCardRanks[cards.size - 1],
        firstKicker = flushCardRanks[cards.size - 2],
        secondKicker = flushCardRanks[cards.size - 3],
        thirdKicker = flushCardRanks[cards.size - 4],
        fourthKicker = flushCardRanks[cards.size - 5],
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
    val cardRanks = cardList.map { it.cardRank }.sorted()
    val threeOfAKindIndex = (4 downTo 0).firstNotNullOfOrNull { getMatchingIndex(cardRanks, it, 3) }
    return if (threeOfAKindIndex != null) {
        val cardRanksMinusThreeOfAKind = removeCardRanksStartingAtIndex(cardRanks, threeOfAKindIndex, 3)
        HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = cardRanks[threeOfAKindIndex],
            firstKicker = cardRanksMinusThreeOfAKind[3],
            secondKicker = cardRanksMinusThreeOfAKind[2],
        )
    } else null
}

fun evaluateTwoPair(cardList: List<Card>): HandEvaluation? {
    val cardRanks =  cardList.map { it.cardRank }.sorted()
    val matchingTopPairIndex = (5 downTo 2).firstNotNullOfOrNull { getMatchingIndex(cardRanks, it, 2) }
    return if (matchingTopPairIndex != null) {
        val cardRanksMinusTopPair = removeCardRanksStartingAtIndex(cardRanks, matchingTopPairIndex, 2)
        val matchingSecondPairIndex = (3 downTo 0).firstNotNullOfOrNull { getMatchingIndex(cardRanksMinusTopPair, it, 2) }
        if (matchingSecondPairIndex != null) {
            val cardRanksMinusBothPairs = removeCardRanksStartingAtIndex(cardRanksMinusTopPair, matchingSecondPairIndex, 2)
            HandEvaluation(
                handRanking = HandRanking.TWO_PAIR,
                primaryCardRank = cardRanks[matchingTopPairIndex],
                secondaryCardRank = cardRanksMinusTopPair[matchingSecondPairIndex],
                firstKicker = cardRanksMinusBothPairs[2],
            )
        } else null
    } else null
}

fun evaluateOnePair(cardList: List<Card>): HandEvaluation? {
    val cardRanks = cardList.map { it.cardRank }.sorted()
    val matchingIndex = (5 downTo 0).firstNotNullOfOrNull { getMatchingIndex(cardRanks, it, 2) }
    return if (matchingIndex != null) {
        val cardRanksMinusPair = removeCardRanksStartingAtIndex(cardRanks, matchingIndex, 2)
        HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = cardRanks[matchingIndex],
            firstKicker = cardRanksMinusPair[4],
            secondKicker = cardRanksMinusPair[3],
            thirdKicker = cardRanksMinusPair[2],
        )
    } else null
}

fun evaluateHighCard(cardList: List<Card>): HandEvaluation {
    val cardRanks = cardList.map { it.cardRank }.sorted()
    return HandEvaluation(
        handRanking = HandRanking.HIGH_CARD,
        primaryCardRank = cardRanks[6],
        firstKicker = cardRanks[5],
        secondKicker = cardRanks[4],
        thirdKicker = cardRanks[3],
        fourthKicker = cardRanks[2],
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

private val straightCardRanks = mapOf(
    CardRank.ACE to listOf(CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE),
    CardRank.KING to listOf(CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING),
    CardRank.QUEEN to listOf(CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN),
    CardRank.JACK to listOf(CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK),
    CardRank.TEN to listOf(CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN),
    CardRank.NINE to listOf(CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE),
    CardRank.EIGHT to listOf(CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT),
    CardRank.SEVEN to listOf(CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN),
    CardRank.SIX to listOf(CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX),
    CardRank.FIVE to listOf(CardRank.ACE, CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE),
)

private fun findCardRankOfHighestStraight(cardList: List<Card>): CardRank? {
    val cardRanks = cardList.map { it.cardRank }
    return straightCardRanks.entries.firstOrNull { cardRanks.containsAll(it.value) }?.key
}

private fun getMatchingIndex(cardList: List<CardRank>, startIndex: Int, numberOfMatches: Int): Int? {
    return if (cardList[startIndex] === cardList[startIndex + (numberOfMatches - 1)]) {
        startIndex
    } else null
}

private fun removeCardRanksStartingAtIndex(cardRanks: List<CardRank>, startIndex: Int, numberToRemove: Int): List<CardRank> {
    val mutableCardRanks = cardRanks.toMutableList()
    (1..numberToRemove).forEach { _ -> mutableCardRanks.removeAt(startIndex) }
    return mutableCardRanks
}
