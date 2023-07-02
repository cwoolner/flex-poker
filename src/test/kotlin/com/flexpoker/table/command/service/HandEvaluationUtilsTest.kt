package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.aggregate.HandEvaluation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class HandEvaluationUtilsTest {

    @Test
    fun testStraightFlush() {
        val actual1 = evaluateStraightFlush(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.KING, CardSuit.DIAMONDS),
            Card(0, CardRank.JACK, CardSuit.DIAMONDS),
            Card(0, CardRank.EIGHT, CardSuit.DIAMONDS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TEN, CardSuit.DIAMONDS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT_FLUSH,
            primaryCardRank = CardRank.ACE,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateStraightFlush(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testFourOfAKind() {
        val actual1 = evaluateFourOfAKind(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.ACE, CardSuit.CLUBS),
            Card(0, CardRank.TWO, CardSuit.DIAMONDS),
            Card(0, CardRank.ACE, CardSuit.SPADES),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.EIGHT, CardSuit.DIAMONDS),
            Card(0, CardRank.ACE, CardSuit.HEARTS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateFourOfAKind(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testFullHouse() {
        val actual1 = evaluateFullHouse(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.ACE, CardSuit.CLUBS),
            Card(0, CardRank.TWO, CardSuit.DIAMONDS),
            Card(0, CardRank.EIGHT, CardSuit.SPADES),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.EIGHT, CardSuit.DIAMONDS),
            Card(0, CardRank.ACE, CardSuit.HEARTS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.FULL_HOUSE,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.EIGHT,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateFullHouse(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testFlush() {
        val actual1 = evaluateFlush(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.THREE, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.DIAMONDS),
            Card(0, CardRank.EIGHT, CardSuit.DIAMONDS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.SEVEN, CardSuit.DIAMONDS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.QUEEN,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.THREE,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateFlush(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testStraight() {
        val actual1 = evaluateStraight(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.JACK, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TEN, CardSuit.CLUBS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.ACE,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateStraight(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testThreeOfAKind() {
        val actual1 = evaluateThreeOfAKind(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.QUEEN,
            firstKicker = CardRank.ACE,
            secondKicker = CardRank.KING,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateThreeOfAKind(listOf(
            Card(0, CardRank.ACE, CardSuit.DIAMONDS),
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.EIGHT, CardSuit.CLUBS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(0, CardRank.TWO, CardSuit.CLUBS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testTwoPair() {
        val actual1 = evaluateTwoPair(listOf(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.HEARTS),
            Card(0, CardRank.FOUR, CardSuit.HEARTS),
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.SPADES),
            Card(0, CardRank.NINE, CardSuit.HEARTS),
            Card(0, CardRank.EIGHT, CardSuit.HEARTS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.TWO_PAIR,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.TWO,
            firstKicker = CardRank.NINE,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateTwoPair(listOf(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.HEARTS),
            Card(0, CardRank.FOUR, CardSuit.HEARTS),
            Card(0, CardRank.THREE, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.SPADES),
            Card(0, CardRank.NINE, CardSuit.HEARTS),
            Card(0, CardRank.EIGHT, CardSuit.HEARTS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testOnePair() {
        val actual1 = evaluateOnePair(listOf(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.HEARTS),
            Card(0, CardRank.FOUR, CardSuit.HEARTS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.SPADES),
            Card(0, CardRank.NINE, CardSuit.HEARTS),
            Card(0, CardRank.EIGHT, CardSuit.HEARTS),
        ))
        val expected1 = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.EIGHT,
        )
        assertEquals(expected1, actual1)

        val actual2 = evaluateOnePair(listOf(
            Card(0, CardRank.TWO, CardSuit.DIAMONDS),
            Card(0, CardRank.KING, CardSuit.HEARTS),
            Card(0, CardRank.FOUR, CardSuit.HEARTS),
            Card(0, CardRank.SEVEN, CardSuit.SPADES),
            Card(0, CardRank.ACE, CardSuit.HEARTS),
            Card(0, CardRank.NINE, CardSuit.CLUBS),
            Card(0, CardRank.EIGHT, CardSuit.HEARTS),
        ))
        assertNull(actual2)
    }

    @Test
    fun testHighCard() {
        val actual = evaluateHighCard(listOf(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.SPADES),
            Card(0, CardRank.FOUR, CardSuit.HEARTS),
            Card(0, CardRank.KING, CardSuit.DIAMONDS),
            Card(0, CardRank.EIGHT, CardSuit.HEARTS),
            Card(0, CardRank.NINE, CardSuit.CLUBS),
            Card(0, CardRank.TEN, CardSuit.HEARTS),
        ))
        val expected = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.NINE,
            fourthKicker = CardRank.EIGHT,
        )
        assertEquals(expected, actual)
    }
}