package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@UnitTestClass
class HandEvaluationComparisonTest {

    @Test
    fun testCompareAll() {
        val straightFlush = HandEvaluation(
            handRanking = HandRanking.STRAIGHT_FLUSH,
            primaryCardRank = CardRank.ACE,
        )
        val fourOfAKind = HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
        )
        val fullHouse = HandEvaluation(
            handRanking = HandRanking.FULL_HOUSE,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.EIGHT,
        )
        val flush = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.QUEEN,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.THREE,
        )
        val straight = HandEvaluation(
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.ACE,
        )
        val threeOfAKind = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.QUEEN,
            firstKicker = CardRank.ACE,
            secondKicker = CardRank.KING,
        )
        val twoPair = HandEvaluation(
            handRanking = HandRanking.TWO_PAIR,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.TWO,
            firstKicker = CardRank.NINE,
        )
        val onePair = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.EIGHT,
        )
        val highCard = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.NINE,
            fourthKicker = CardRank.EIGHT,
        )
        val expected = listOf(
            straightFlush,
            fourOfAKind,
            fullHouse,
            flush,
            straight,
            threeOfAKind,
            twoPair,
            onePair,
            highCard,
        )
        val actual = listOf(
            flush,
            fourOfAKind,
            straight,
            onePair,
            threeOfAKind,
            highCard,
            twoPair,
            fullHouse,
            straightFlush,
        ).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testStraightFlush() {
        val straightFlush1 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT_FLUSH,
            primaryCardRank = CardRank.ACE,
        )
        val straightFlush2 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT_FLUSH,
            primaryCardRank = CardRank.THREE,
        )
        val straightFlush3 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT_FLUSH,
            primaryCardRank = CardRank.EIGHT,
        )
        val expected = listOf(straightFlush1, straightFlush3, straightFlush2)
        val actual = listOf(straightFlush1, straightFlush2, straightFlush3).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testFourOfAKind() {
        val fourOfAKind1 = HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.TWO,
        )
        val fourOfAKind2 = HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.FOUR,
        )
        val fourOfAKind3 = HandEvaluation(
            handRanking = HandRanking.FOUR_OF_A_KIND,
            primaryCardRank = CardRank.SEVEN,
            firstKicker = CardRank.ACE,
        )
        val expected = listOf(fourOfAKind2, fourOfAKind1, fourOfAKind3)
        val actual = listOf(fourOfAKind1, fourOfAKind2, fourOfAKind3).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testFullHouse() {
        val fullHouse1 = HandEvaluation(
            handRanking = HandRanking.FULL_HOUSE,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.EIGHT,
        )
        val fullHouse2 = HandEvaluation(
            handRanking = HandRanking.FULL_HOUSE,
            primaryCardRank = CardRank.THREE,
            secondaryCardRank = CardRank.KING,
        )
        val fullHouse3 = HandEvaluation(
            handRanking = HandRanking.FULL_HOUSE,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.JACK,
        )
        val expected = listOf(fullHouse3, fullHouse1, fullHouse2)
        val actual = listOf(fullHouse1, fullHouse2, fullHouse3).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testFlush() {
        val flush1 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.TEN,
            firstKicker = CardRank.NINE,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.TWO,
        )
        val flush2 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.QUEEN,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.FOUR,
        )
        val flush3 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.QUEEN,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.THREE,
        )
        val flush4 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.THREE,
        )
        val flush5 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.SEVEN,
            fourthKicker = CardRank.THREE,
        )
        val flush6 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.EIGHT,
            fourthKicker = CardRank.THREE,
        )
        val flush7 = HandEvaluation(
            handRanking = HandRanking.FLUSH,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.EIGHT,
            fourthKicker = CardRank.FOUR,
        )
        val expected = listOf(flush7, flush6, flush5, flush4, flush2, flush3, flush1)
        val actual = listOf(flush1, flush2, flush3, flush4, flush5, flush6, flush7).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testStraight() {
        val straight1 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.ACE,
        )
        val straight2 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.EIGHT,
        )
        val straight3 = HandEvaluation(
            handRanking = HandRanking.STRAIGHT,
            primaryCardRank = CardRank.TEN,
        )
        val expected = listOf(straight1, straight3, straight2)
        val actual = listOf(straight1, straight2, straight3).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testThreeOfAKind() {
        val threeOfAKind1 = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.QUEEN,
            firstKicker = CardRank.ACE,
            secondKicker = CardRank.TWO,
        )
        val threeOfAKind2 = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.QUEEN,
            firstKicker = CardRank.ACE,
            secondKicker = CardRank.KING,
        )
        val threeOfAKind3 = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.QUEEN,
            firstKicker = CardRank.TEN,
            secondKicker = CardRank.EIGHT,
        )
        val threeOfAKind4 = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.EIGHT,
            secondKicker = CardRank.SIX,
        )
        val threeOfAKind5 = HandEvaluation(
            handRanking = HandRanking.THREE_OF_A_KIND,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.EIGHT,
            secondKicker = CardRank.FIVE,
        )
        val expected = listOf(threeOfAKind4, threeOfAKind5, threeOfAKind2, threeOfAKind1, threeOfAKind3)
        val actual = listOf(threeOfAKind1, threeOfAKind2, threeOfAKind3, threeOfAKind4, threeOfAKind5).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testTwoPair() {
        val twoPair1 = HandEvaluation(
            handRanking = HandRanking.TWO_PAIR,
            primaryCardRank = CardRank.KING,
            secondaryCardRank = CardRank.FIVE,
            firstKicker = CardRank.NINE,
        )
        val twoPair2 = HandEvaluation(
            handRanking = HandRanking.TWO_PAIR,
            primaryCardRank = CardRank.ACE,
            secondaryCardRank = CardRank.EIGHT,
            firstKicker = CardRank.TEN,
        )
        val twoPair3 = HandEvaluation(
            handRanking = HandRanking.TWO_PAIR,
            primaryCardRank = CardRank.KING,
            secondaryCardRank = CardRank.SIX,
            firstKicker = CardRank.NINE,
        )
        val twoPair4 = HandEvaluation(
            handRanking = HandRanking.TWO_PAIR,
            primaryCardRank = CardRank.KING,
            secondaryCardRank = CardRank.SIX,
            firstKicker = CardRank.EIGHT,
        )
        val expected = listOf(twoPair2, twoPair3, twoPair4, twoPair1)
        val actual = listOf(twoPair1, twoPair2, twoPair3, twoPair4).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testOnePair() {
        val onePair1 = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.QUEEN,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.EIGHT,
        )
        val onePair2 = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.SEVEN,
        )
        val onePair3 = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.SEVEN,
            firstKicker = CardRank.ACE,
            secondKicker = CardRank.FIVE,
            thirdKicker = CardRank.TWO,
        )
        val onePair4 = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.EIGHT,
        )
        val onePair5 = HandEvaluation(
            handRanking = HandRanking.ONE_PAIR,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.NINE,
            thirdKicker = CardRank.SIX,
        )
        val expected = listOf(onePair2, onePair4, onePair5, onePair1, onePair3)
        val actual = listOf(onePair1, onePair2, onePair3, onePair4, onePair5).sortedDescending()
        assertEquals(expected, actual)
    }

    @Test
    fun testHighCard() {
        val highCard1 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.TEN,
            firstKicker = CardRank.NINE,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.FIVE,
            fourthKicker = CardRank.TWO,
        )
        val highCard2 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.NINE,
            fourthKicker = CardRank.EIGHT,
        )
        val highCard3 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.KING,
            secondKicker = CardRank.QUEEN,
            thirdKicker = CardRank.NINE,
            fourthKicker = CardRank.EIGHT,
        )
        val highCard4 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.JACK,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.NINE,
            fourthKicker = CardRank.SIX,
        )
        val highCard5 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.TEN,
            firstKicker = CardRank.NINE,
            secondKicker = CardRank.EIGHT,
            thirdKicker = CardRank.FOUR,
            fourthKicker = CardRank.TWO,
        )
        val highCard6 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.JACK,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.EIGHT,
            fourthKicker = CardRank.SIX,
        )
        val highCard7 = HandEvaluation(
            handRanking = HandRanking.HIGH_CARD,
            primaryCardRank = CardRank.ACE,
            firstKicker = CardRank.JACK,
            secondKicker = CardRank.TEN,
            thirdKicker = CardRank.EIGHT,
            fourthKicker = CardRank.SEVEN,
        )
        val expected = listOf(highCard3, highCard2, highCard4, highCard7, highCard6, highCard1, highCard5)
        val actual = listOf(highCard1, highCard2, highCard3, highCard4, highCard5, highCard6, highCard7).sortedDescending()
        assertEquals(expected, actual)
    }

}