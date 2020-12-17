package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import com.flexpoker.table.command.aggregate.CommonCards
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.UUID

class DefaultHandEvaluatorServiceTest {

    private val bso = DefaultHandEvaluatorService()

    @Test
    fun testDeterminePossibleHands() {
        testDeterminePossibleHandsStraightFlushOnBoard()
        testDeterminePossibleHandsFourOfAKindOnBoard()
        testDeterminePossibleHandsFullHouseOnBoard()
        testDeterminePossibleHandsFlushOnBoard()
        testDeterminePossibleHandsStraightOnBoard()
        testDeterminePossibleHandsThreeOfAKindOnBoard()
        testDeterminePossibleHandsTwoPairOnBoard()
        testDeterminePossibleHandsOnePairOnBoard()
        testDeterminePossibleHandsScenario1()
        testDeterminePossibleHandsScenario2()
        testDeterminePossibleHandsScenario3()
        testDeterminePossibleHandsScenario4()
        testDeterminePossibleHandsScenario5()
    }

    @Test
    fun testDetermineHandEvaluation() {
        testDetermineHandEvaluationScenario1()
        testDetermineHandEvaluationScenario2()
        testDetermineHandEvaluationScenario3()
        testDetermineHandEvaluationScenario4()
    }

    private fun testDetermineHandEvaluationScenario1() {
        val card1 = Card(0, CardRank.FOUR, CardSuit.SPADES)
        val card2 = Card(0, CardRank.JACK, CardSuit.SPADES)
        val card3 = Card(0, CardRank.EIGHT, CardSuit.HEARTS)
        val card4 = Card(0, CardRank.FOUR, CardSuit.HEARTS)
        val card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val id3 = UUID.randomUUID()
        val id4 = UUID.randomUUID()
        val id5 = UUID.randomUUID()
        val id6 = UUID.randomUUID()
        val id8 = UUID.randomUUID()
        val pocketCardsList = ArrayList<PocketCards>()
        val pocketCards1 = PocketCards(
            Card(0, CardRank.NINE, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.CLUBS)
        )
        val pocketCards2 = PocketCards(
            Card(0, CardRank.QUEEN, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.CLUBS)
        )
        val pocketCards3 = PocketCards(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.HEARTS)
        )
        val pocketCards4 = PocketCards(
            Card(0, CardRank.FOUR, CardSuit.CLUBS),
            Card(0, CardRank.FOUR, CardSuit.DIAMONDS)
        )
        val pocketCards5 = PocketCards(
            Card(0, CardRank.FIVE, CardSuit.CLUBS),
            Card(0, CardRank.FOUR, CardSuit.DIAMONDS)
        )
        val pocketCards6 = PocketCards(
            Card(0, CardRank.FOUR, CardSuit.CLUBS),
            Card(0, CardRank.EIGHT, CardSuit.CLUBS)
        )
        val pocketCards8 = PocketCards(
            Card(0, CardRank.QUEEN, CardSuit.CLUBS),
            Card(0, CardRank.QUEEN, CardSuit.DIAMONDS)
        )
        pocketCardsList.add(pocketCards1)
        pocketCardsList.add(pocketCards2)
        pocketCardsList.add(pocketCards3)
        pocketCardsList.add(pocketCards4)
        pocketCardsList.add(pocketCards5)
        pocketCardsList.add(pocketCards6)
        pocketCardsList.add(pocketCards8)
        val handEvaluations = bso.determineHandEvaluation(
            flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings
        )
        val handEvaluation1 = handEvaluations[pocketCards1]
        val handEvaluation2 = handEvaluations[pocketCards2]
        val handEvaluation3 = handEvaluations[pocketCards3]
        val handEvaluation4 = handEvaluations[pocketCards4]
        val handEvaluation5 = handEvaluations[pocketCards5]
        val handEvaluation6 = handEvaluations[pocketCards6]
        val handEvaluation8 = handEvaluations[pocketCards8]
        handEvaluation1!!.playerId = id1
        handEvaluation2!!.playerId = id2
        handEvaluation3!!.playerId = id3
        handEvaluation4!!.playerId = id4
        handEvaluation5!!.playerId = id5
        handEvaluation6!!.playerId = id6
        handEvaluation8!!.playerId = id8
        assertEquals(HandRanking.ONE_PAIR, handEvaluation1.handRanking)
        assertEquals(CardRank.FOUR, handEvaluation1.primaryCardRank)
        assertEquals(CardRank.ACE, handEvaluation1.firstKicker)
        assertEquals(CardRank.QUEEN, handEvaluation1.secondKicker)
        assertEquals(CardRank.JACK, handEvaluation1.thirdKicker)
        assertEquals(id1, handEvaluation1.playerId)
        assertEquals(HandRanking.TWO_PAIR, handEvaluation2.handRanking)
        assertEquals(CardRank.QUEEN, handEvaluation2.primaryCardRank)
        assertEquals(CardRank.FOUR, handEvaluation2.secondaryCardRank)
        assertEquals(CardRank.ACE, handEvaluation2.firstKicker)
        assertEquals(id2, handEvaluation2.playerId)
        assertEquals(HandRanking.FLUSH, handEvaluation3.handRanking)
        assertEquals(CardRank.ACE, handEvaluation3.primaryCardRank)
        assertEquals(CardRank.QUEEN, handEvaluation3.firstKicker)
        assertEquals(CardRank.EIGHT, handEvaluation3.secondKicker)
        assertEquals(CardRank.FOUR, handEvaluation3.thirdKicker)
        assertEquals(CardRank.TWO, handEvaluation3.fourthKicker)
        assertEquals(id3, handEvaluation3.playerId)
        assertEquals(HandRanking.FOUR_OF_A_KIND, handEvaluation4.handRanking)
        assertEquals(CardRank.FOUR, handEvaluation4.primaryCardRank)
        assertEquals(CardRank.QUEEN, handEvaluation4.firstKicker)
        assertEquals(id4, handEvaluation4.playerId)
        assertEquals(HandRanking.THREE_OF_A_KIND, handEvaluation5.handRanking)
        assertEquals(CardRank.FOUR, handEvaluation5.primaryCardRank)
        assertEquals(CardRank.QUEEN, handEvaluation5.firstKicker)
        assertEquals(CardRank.JACK, handEvaluation5.secondKicker)
        assertEquals(id5, handEvaluation5.playerId)
        assertEquals(HandRanking.FULL_HOUSE, handEvaluation6.handRanking)
        assertEquals(CardRank.FOUR, handEvaluation6.primaryCardRank)
        assertEquals(CardRank.EIGHT, handEvaluation6.secondaryCardRank)
        assertEquals(id6, handEvaluation6.playerId)
        assertEquals(HandRanking.FULL_HOUSE, handEvaluation8.handRanking)
        assertEquals(CardRank.QUEEN, handEvaluation8.primaryCardRank)
        assertEquals(CardRank.FOUR, handEvaluation8.secondaryCardRank)
        assertEquals(id8, handEvaluation8.playerId)
    }

    private fun testDetermineHandEvaluationScenario2() {
        val card1 = Card(0, CardRank.KING, CardSuit.HEARTS)
        val card2 = Card(0, CardRank.JACK, CardSuit.HEARTS)
        val card3 = Card(0, CardRank.NINE, CardSuit.HEARTS)
        val card4 = Card(0, CardRank.TEN, CardSuit.HEARTS)
        val card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val id3 = UUID.randomUUID()
        val pocketCardsList = ArrayList<PocketCards>()
        val pocketCards1 = PocketCards(
            Card(0, CardRank.EIGHT, CardSuit.HEARTS),
            Card(0, CardRank.ACE, CardSuit.CLUBS)
        )
        val pocketCards2 = PocketCards(
            Card(0, CardRank.QUEEN, CardSuit.SPADES),
            Card(0, CardRank.ACE, CardSuit.HEARTS)
        )
        val pocketCards3 = PocketCards(
            Card(0, CardRank.QUEEN, CardSuit.SPADES),
            Card(0, CardRank.ACE, CardSuit.DIAMONDS)
        )
        pocketCardsList.add(pocketCards1)
        pocketCardsList.add(pocketCards2)
        pocketCardsList.add(pocketCards3)
        val handEvaluations = bso.determineHandEvaluation(
            flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings
        )
        val handEvaluation1 = handEvaluations[pocketCards1]
        val handEvaluation2 = handEvaluations[pocketCards2]
        val handEvaluation3 = handEvaluations[pocketCards3]
        handEvaluation1!!.playerId = id1
        handEvaluation2!!.playerId = id2
        handEvaluation3!!.playerId = id3
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation1.handRanking)
        assertEquals(CardRank.KING, handEvaluation1.primaryCardRank)
        assertEquals(id1, handEvaluation1.playerId)
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation2.handRanking)
        assertEquals(CardRank.ACE, handEvaluation2.primaryCardRank)
        assertEquals(id2, handEvaluation2.playerId)
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation3.handRanking)
        assertEquals(CardRank.KING, handEvaluation3.primaryCardRank)
        assertEquals(id3, handEvaluation3.playerId)
    }

    private fun testDetermineHandEvaluationScenario3() {
        val card1 = Card(0, CardRank.ACE, CardSuit.CLUBS)
        val card2 = Card(0, CardRank.TWO, CardSuit.CLUBS)
        val card3 = Card(0, CardRank.THREE, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.FOUR, CardSuit.CLUBS)
        val card5 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val id3 = UUID.randomUUID()
        val pocketCardsList = ArrayList<PocketCards>()
        val pocketCards1 = PocketCards(
            Card(0, CardRank.FIVE, CardSuit.CLUBS), Card(0, CardRank.ACE, CardSuit.CLUBS)
        )
        val pocketCards2 = PocketCards(
            Card(0, CardRank.FIVE, CardSuit.SPADES), Card(0, CardRank.ACE, CardSuit.HEARTS)
        )
        val pocketCards3 = PocketCards(
            Card(0, CardRank.QUEEN, CardSuit.SPADES), Card(0, CardRank.ACE, CardSuit.DIAMONDS)
        )
        pocketCardsList.add(pocketCards1)
        pocketCardsList.add(pocketCards2)
        pocketCardsList.add(pocketCards3)
        val handEvaluations = bso.determineHandEvaluation(
            flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings
        )
        val handEvaluation1 = handEvaluations[pocketCards1]
        val handEvaluation2 = handEvaluations[pocketCards2]
        val handEvaluation3 = handEvaluations[pocketCards3]
        handEvaluation1!!.playerId = id1
        handEvaluation2!!.playerId = id2
        handEvaluation3!!.playerId = id3
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation1.handRanking)
        assertEquals(CardRank.FIVE, handEvaluation1.primaryCardRank)
        assertEquals(id1, handEvaluation1.playerId)
        assertEquals(HandRanking.STRAIGHT, handEvaluation2.handRanking)
        assertEquals(CardRank.SIX, handEvaluation2.primaryCardRank)
        assertEquals(id2, handEvaluation2.playerId)
        assertEquals(HandRanking.ONE_PAIR, handEvaluation3.handRanking)
        assertEquals(CardRank.ACE, handEvaluation3.primaryCardRank)
        assertEquals(CardRank.QUEEN, handEvaluation3.firstKicker)
        assertEquals(CardRank.SIX, handEvaluation3.secondKicker)
        assertEquals(CardRank.FOUR, handEvaluation3.thirdKicker)
        assertEquals(id3, handEvaluation3.playerId)
    }

    private fun testDetermineHandEvaluationScenario4() {
        val card1 = Card(0, CardRank.KING, CardSuit.SPADES)
        val card2 = Card(0, CardRank.TWO, CardSuit.DIAMONDS)
        val card3 = Card(0, CardRank.QUEEN, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.FOUR, CardSuit.CLUBS)
        val card5 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        val pocketCardsList = ArrayList<PocketCards>()
        val pocketCards1 = PocketCards(
            Card(0, CardRank.FIVE, CardSuit.CLUBS), Card(0, CardRank.ACE, CardSuit.CLUBS)
        )
        val pocketCards2 = PocketCards(
            Card(0, CardRank.THREE, CardSuit.SPADES), Card(0, CardRank.SEVEN, CardSuit.HEARTS)
        )
        pocketCardsList.add(pocketCards1)
        pocketCardsList.add(pocketCards2)
        val handEvaluations = bso.determineHandEvaluation(
            flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings
        )
        val handEvaluation1 = handEvaluations[pocketCards1]
        val handEvaluation2 = handEvaluations[pocketCards2]
        handEvaluation1!!.playerId = id1
        handEvaluation2!!.playerId = id2
        assertEquals(HandRanking.HIGH_CARD, handEvaluation1.handRanking)
        assertEquals(CardRank.ACE, handEvaluation1.primaryCardRank)
        assertEquals(CardRank.KING, handEvaluation1.firstKicker)
        assertEquals(CardRank.QUEEN, handEvaluation1.secondKicker)
        assertEquals(CardRank.SIX, handEvaluation1.thirdKicker)
        assertEquals(CardRank.FIVE, handEvaluation1.fourthKicker)
        assertEquals(id1, handEvaluation1.playerId)
        assertEquals(HandRanking.HIGH_CARD, handEvaluation2.handRanking)
        assertEquals(CardRank.KING, handEvaluation2.primaryCardRank)
        assertEquals(CardRank.QUEEN, handEvaluation2.firstKicker)
        assertEquals(CardRank.SEVEN, handEvaluation2.secondKicker)
        assertEquals(CardRank.SIX, handEvaluation2.thirdKicker)
        assertEquals(CardRank.FOUR, handEvaluation2.fourthKicker)
        assertEquals(id2, handEvaluation2.playerId)
    }

    private fun testDeterminePossibleHandsScenario1() {
        val card1 = Card(0, CardRank.ACE, CardSuit.HEARTS)
        val card2 = Card(0, CardRank.TWO, CardSuit.DIAMONDS)
        val card3 = Card(0, CardRank.SEVEN, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.EIGHT, CardSuit.SPADES)
        val card5 = Card(0, CardRank.KING, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(4, handRankings.size)
        assertEquals(HandRanking.HIGH_CARD, handRankings[0])
        assertEquals(HandRanking.ONE_PAIR, handRankings[1])
        assertEquals(HandRanking.TWO_PAIR, handRankings[2])
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[3])
    }

    private fun testDeterminePossibleHandsScenario2() {
        val card1 = Card(0, CardRank.ACE, CardSuit.HEARTS)
        val card2 = Card(0, CardRank.KING, CardSuit.HEARTS)
        val card3 = Card(0, CardRank.SEVEN, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.EIGHT, CardSuit.SPADES)
        val card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(7, handRankings.size)
        assertEquals(HandRanking.HIGH_CARD, handRankings[0])
        assertEquals(HandRanking.ONE_PAIR, handRankings[1])
        assertEquals(HandRanking.TWO_PAIR, handRankings[2])
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[3])
        assertEquals(HandRanking.STRAIGHT, handRankings[4])
        assertEquals(HandRanking.FLUSH, handRankings[5])
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings[6])
    }

    private fun testDeterminePossibleHandsScenario3() {
        val card1 = Card(0, CardRank.THREE, CardSuit.CLUBS)
        val card2 = Card(0, CardRank.NINE, CardSuit.SPADES)
        val card3 = Card(0, CardRank.TWO, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.SIX, CardSuit.CLUBS)
        val card5 = Card(0, CardRank.THREE, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(8, handRankings.size)
        assertEquals(HandRanking.ONE_PAIR, handRankings[0])
        assertEquals(HandRanking.TWO_PAIR, handRankings[1])
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[2])
        assertEquals(HandRanking.STRAIGHT, handRankings[3])
        assertEquals(HandRanking.FLUSH, handRankings[4])
        assertEquals(HandRanking.FULL_HOUSE, handRankings[5])
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[6])
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings[7])
    }

    private fun testDeterminePossibleHandsScenario4() {
        val card1 = Card(0, CardRank.TEN, CardSuit.CLUBS)
        val card2 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        val card3 = Card(0, CardRank.JACK, CardSuit.DIAMONDS)
        val card4 = Card(0, CardRank.NINE, CardSuit.CLUBS)
        val card5 = Card(0, CardRank.THREE, CardSuit.CLUBS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(6, handRankings.size)
        assertEquals(HandRanking.HIGH_CARD, handRankings[0])
        assertEquals(HandRanking.ONE_PAIR, handRankings[1])
        assertEquals(HandRanking.TWO_PAIR, handRankings[2])
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[3])
        assertEquals(HandRanking.STRAIGHT, handRankings[4])
        assertEquals(HandRanking.FLUSH, handRankings[5])
    }

    private fun testDeterminePossibleHandsScenario5() {
        val card1 = Card(0, CardRank.EIGHT, CardSuit.CLUBS)
        val card2 = Card(0, CardRank.FOUR, CardSuit.DIAMONDS)
        val card3 = Card(0, CardRank.ACE, CardSuit.DIAMONDS)
        val card4 = Card(0, CardRank.EIGHT, CardSuit.DIAMONDS)
        val card5 = Card(0, CardRank.FIVE, CardSuit.DIAMONDS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        val handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(8, handRankings.size)
        assertEquals(HandRanking.ONE_PAIR, handRankings[0])
        assertEquals(HandRanking.TWO_PAIR, handRankings[1])
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[2])
        assertEquals(HandRanking.STRAIGHT, handRankings[3])
        assertEquals(HandRanking.FLUSH, handRankings[4])
        assertEquals(HandRanking.FULL_HOUSE, handRankings[5])
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[6])
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings[7])
    }

    private fun testDeterminePossibleHandsStraightFlushOnBoard() {
        verifyStraightFlushOnBoardHelper(CardRank.FIVE)
        verifyStraightFlushOnBoardHelper(CardRank.SIX)
        verifyStraightFlushOnBoardHelper(CardRank.SEVEN)
        verifyStraightFlushOnBoardHelper(CardRank.EIGHT)
        verifyStraightFlushOnBoardHelper(CardRank.NINE)
        verifyStraightFlushOnBoardHelper(CardRank.TEN)
        verifyStraightFlushOnBoardHelper(CardRank.JACK)
        verifyStraightFlushOnBoardHelper(CardRank.QUEEN)
        verifyStraightFlushOnBoardHelper(CardRank.KING)
        verifyStraightFlushOnBoardHelper(CardRank.ACE)
    }

    private fun testDeterminePossibleHandsFourOfAKindOnBoard() {
        verifyFourOfAKindOnBoardHelper(CardRank.TWO)
        verifyFourOfAKindOnBoardHelper(CardRank.THREE)
        verifyFourOfAKindOnBoardHelper(CardRank.FOUR)
        verifyFourOfAKindOnBoardHelper(CardRank.FIVE)
        verifyStraightFlushOnBoardHelper(CardRank.SIX)
        verifyFourOfAKindOnBoardHelper(CardRank.SEVEN)
        verifyFourOfAKindOnBoardHelper(CardRank.EIGHT)
        verifyStraightFlushOnBoardHelper(CardRank.NINE)
        verifyFourOfAKindOnBoardHelper(CardRank.TEN)
        verifyFourOfAKindOnBoardHelper(CardRank.JACK)
        verifyFourOfAKindOnBoardHelper(CardRank.QUEEN)
        verifyStraightFlushOnBoardHelper(CardRank.KING)
        verifyFourOfAKindOnBoardHelper(CardRank.ACE)
    }

    private fun testDeterminePossibleHandsFullHouseOnBoard() {
        verifyFullHouseOnBoardHelper(CardRank.TWO, CardRank.ACE)
        verifyFullHouseOnBoardHelper(CardRank.ACE, CardRank.TWO)
        verifyFullHouseOnBoardHelper(CardRank.KING, CardRank.ACE)
        verifyFullHouseOnBoardHelper(CardRank.ACE, CardRank.KING)
        verifyFullHouseOnBoardHelper(CardRank.KING, CardRank.QUEEN)
        verifyFullHouseOnBoardHelper(CardRank.EIGHT, CardRank.SEVEN)
    }

    private fun testDeterminePossibleHandsFlushOnBoard() {
        var card1 = Card(0, CardRank.ACE, CardSuit.HEARTS)
        var card2 = Card(0, CardRank.TWO, CardSuit.HEARTS)
        var card3 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        var card4 = Card(0, CardRank.SEVEN, CardSuit.HEARTS)
        var card5 = Card(0, CardRank.KING, CardSuit.HEARTS)
        var flopCards = FlopCards(card1, card2, card3)
        var turnCard = TurnCard(card4)
        var riverCard = RiverCard(card5)
        var handRankings: List<HandRanking?> = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(1, handRankings.size)
        assertEquals(HandRanking.FLUSH, handRankings[0])
        card1 = Card(0, CardRank.THREE, CardSuit.SPADES)
        card2 = Card(0, CardRank.TWO, CardSuit.SPADES)
        card3 = Card(0, CardRank.EIGHT, CardSuit.SPADES)
        card4 = Card(0, CardRank.NINE, CardSuit.SPADES)
        card5 = Card(0, CardRank.KING, CardSuit.SPADES)
        flopCards = FlopCards(card1, card2, card3)
        turnCard = TurnCard(card4)
        riverCard = RiverCard(card5)
        handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard)
        assertEquals(1, handRankings.size)
        assertEquals(HandRanking.FLUSH, handRankings[0])
    }

    private fun testDeterminePossibleHandsStraightOnBoard() {
        verifyStraightOnBoardHelper(CardRank.FIVE)
        verifyStraightOnBoardHelper(CardRank.SIX)
        verifyStraightOnBoardHelper(CardRank.SEVEN)
        verifyStraightOnBoardHelper(CardRank.EIGHT)
        verifyStraightOnBoardHelper(CardRank.NINE)
        verifyStraightOnBoardHelper(CardRank.TEN)
        verifyStraightOnBoardHelper(CardRank.JACK)
        verifyStraightOnBoardHelper(CardRank.QUEEN)
        verifyStraightOnBoardHelper(CardRank.KING)
        verifyStraightOnBoardHelper(CardRank.ACE)
    }

    private fun testDeterminePossibleHandsThreeOfAKindOnBoard() {
        verifyThreeOfAKindOnBoardHelper(CardRank.TWO)
        verifyThreeOfAKindOnBoardHelper(CardRank.THREE)
        verifyThreeOfAKindOnBoardHelper(CardRank.FOUR)
        verifyThreeOfAKindOnBoardHelper(CardRank.FIVE)
        verifyThreeOfAKindOnBoardHelper(CardRank.SIX)
        verifyThreeOfAKindOnBoardHelper(CardRank.SEVEN)
        verifyThreeOfAKindOnBoardHelper(CardRank.EIGHT)
        verifyThreeOfAKindOnBoardHelper(CardRank.NINE)
        verifyThreeOfAKindOnBoardHelper(CardRank.TEN)
        verifyThreeOfAKindOnBoardHelper(CardRank.JACK)
        verifyThreeOfAKindOnBoardHelper(CardRank.QUEEN)
        verifyThreeOfAKindOnBoardHelper(CardRank.KING)
        verifyThreeOfAKindOnBoardHelper(CardRank.ACE)
    }

    private fun testDeterminePossibleHandsTwoPairOnBoard() {
        verifyTwoPairOnBoardHelper(CardRank.TWO, CardRank.KING)
        verifyTwoPairOnBoardHelper(CardRank.THREE, CardRank.ACE)
        verifyTwoPairOnBoardHelper(CardRank.FOUR, CardRank.SEVEN)
        verifyTwoPairOnBoardHelper(CardRank.FIVE, CardRank.EIGHT)
        verifyTwoPairOnBoardHelper(CardRank.SIX, CardRank.ACE)
        verifyTwoPairOnBoardHelper(CardRank.SEVEN, CardRank.TWO)
        verifyTwoPairOnBoardHelper(CardRank.EIGHT, CardRank.NINE)
        verifyTwoPairOnBoardHelper(CardRank.NINE, CardRank.TEN)
        verifyTwoPairOnBoardHelper(CardRank.TEN, CardRank.JACK)
        verifyTwoPairOnBoardHelper(CardRank.JACK, CardRank.THREE)
        verifyTwoPairOnBoardHelper(CardRank.QUEEN, CardRank.KING)
        verifyTwoPairOnBoardHelper(CardRank.KING, CardRank.SIX)
        verifyTwoPairOnBoardHelper(CardRank.ACE, CardRank.KING)
    }

    private fun testDeterminePossibleHandsOnePairOnBoard() {
        verifyOnePairOnBoardHelper(CardRank.TWO)
        verifyOnePairOnBoardHelper(CardRank.THREE)
        verifyOnePairOnBoardHelper(CardRank.FOUR)
        verifyOnePairOnBoardHelper(CardRank.FIVE)
        verifyOnePairOnBoardHelper(CardRank.SIX)
        verifyOnePairOnBoardHelper(CardRank.SEVEN)
        verifyOnePairOnBoardHelper(CardRank.EIGHT)
        verifyOnePairOnBoardHelper(CardRank.NINE)
        verifyOnePairOnBoardHelper(CardRank.TEN)
        verifyOnePairOnBoardHelper(CardRank.JACK)
        verifyOnePairOnBoardHelper(CardRank.QUEEN)
        verifyOnePairOnBoardHelper(CardRank.KING)
        verifyOnePairOnBoardHelper(CardRank.ACE)
    }

    private fun verifyStraightFlushOnBoardHelper(cardRank: CardRank) {
        val commonCards = createStraightFlush(cardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(1, handRankings.size)
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings[0])
    }

    private fun verifyFourOfAKindOnBoardHelper(cardRank: CardRank) {
        val commonCards = createFourOfAKind(cardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(1, handRankings.size)
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[0])
    }

    private fun verifyFullHouseOnBoardHelper(primaryCardRank: CardRank, secondaryCardRank: CardRank) {
        val commonCards = createFullHouse(primaryCardRank, secondaryCardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(2, handRankings.size)
        assertEquals(HandRanking.FULL_HOUSE, handRankings[0])
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[1])
    }

    private fun verifyStraightOnBoardHelper(cardRank: CardRank) {
        val commonCards = createStraight(cardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(1, handRankings.size)
        assertEquals(HandRanking.STRAIGHT, handRankings[0])
    }

    private fun verifyThreeOfAKindOnBoardHelper(cardRank: CardRank) {
        val commonCards = createThreeOfAKind(cardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(3, handRankings.size)
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[0])
        assertEquals(HandRanking.FULL_HOUSE, handRankings[1])
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[2])
    }

    private fun verifyTwoPairOnBoardHelper(primaryCardRank: CardRank, secondaryCardRank: CardRank) {
        val commonCards = createTwoPair(primaryCardRank, secondaryCardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(3, handRankings.size)
        assertEquals(HandRanking.TWO_PAIR, handRankings[0])
        assertEquals(HandRanking.FULL_HOUSE, handRankings[1])
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[2])
    }

    private fun verifyOnePairOnBoardHelper(cardRank: CardRank) {
        val commonCards = createOnePair(cardRank)
        val handRankings = bso.determinePossibleHands(
            commonCards.flopCards, commonCards.turnCard, commonCards.riverCard
        )
        assertEquals(5, handRankings.size)
        assertEquals(HandRanking.ONE_PAIR, handRankings[0])
        assertEquals(HandRanking.TWO_PAIR, handRankings[1])
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings[2])
        assertEquals(HandRanking.FULL_HOUSE, handRankings[3])
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings[4])
    }

    private fun createThreeOfAKind(cardRank: CardRank): CommonCards {
        val card1 = Card(0, cardRank, CardSuit.CLUBS)
        val card4 = Card(0, cardRank, CardSuit.SPADES)
        val card2 = Card(0, cardRank, CardSuit.DIAMONDS)
        val card3: Card
        val card5: Card
        if (cardRank.ordinal < 7) {
            card3 = Card(0, CardRank.KING, CardSuit.HEARTS)
            card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        } else {
            card3 = Card(0, CardRank.TWO, CardSuit.HEARTS)
            card5 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        }
        val flopCards = FlopCards(card2, card4, card5)
        val turnCard = TurnCard(card3)
        val riverCard = RiverCard(card1)
        return CommonCards(flopCards, turnCard, riverCard)
    }

    private fun createTwoPair(primaryCardRank: CardRank, secondaryCardRank: CardRank): CommonCards {
        val card1 = Card(0, primaryCardRank, CardSuit.CLUBS)
        val card4 = Card(0, primaryCardRank, CardSuit.SPADES)
        val card2 = Card(0, secondaryCardRank, CardSuit.HEARTS)
        val card3 = Card(0, secondaryCardRank, CardSuit.SPADES)
        val card5: Card
        card5 = if (primaryCardRank.ordinal < 7) {
            if (secondaryCardRank == CardRank.KING) {
                Card(0, CardRank.QUEEN, CardSuit.HEARTS)
            } else {
                Card(0, CardRank.KING, CardSuit.HEARTS)
            }
        } else {
            if (secondaryCardRank == CardRank.THREE) {
                Card(0, CardRank.TWO, CardSuit.HEARTS)
            } else {
                Card(0, CardRank.THREE, CardSuit.HEARTS)
            }
        }
        val flopCards = FlopCards(card2, card4, card5)
        val turnCard = TurnCard(card3)
        val riverCard = RiverCard(card1)
        return CommonCards(flopCards, turnCard, riverCard)
    }

    private fun createOnePair(cardRank: CardRank): CommonCards {
        val card1 = Card(0, cardRank, CardSuit.CLUBS)
        val card4 = Card(0, cardRank, CardSuit.SPADES)
        val card2: Card
        val card3: Card
        val card5: Card
        if (cardRank == CardRank.TWO) {
            card2 = Card(0, CardRank.ACE, CardSuit.CLUBS)
            card3 = Card(0, CardRank.SEVEN, CardSuit.DIAMONDS)
            card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        } else if (cardRank == CardRank.SEVEN) {
            card2 = Card(0, CardRank.TWO, CardSuit.CLUBS)
            card3 = Card(0, CardRank.ACE, CardSuit.DIAMONDS)
            card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        } else if (cardRank == CardRank.QUEEN) {
            card2 = Card(0, CardRank.TWO, CardSuit.CLUBS)
            card3 = Card(0, CardRank.SEVEN, CardSuit.DIAMONDS)
            card5 = Card(0, CardRank.KING, CardSuit.HEARTS)
        } else {
            card2 = Card(0, CardRank.TWO, CardSuit.CLUBS)
            card3 = Card(0, CardRank.SEVEN, CardSuit.DIAMONDS)
            card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        }
        val flopCards = FlopCards(card2, card4, card5)
        val turnCard = TurnCard(card3)
        val riverCard = RiverCard(card1)
        return CommonCards(flopCards, turnCard, riverCard)
    }

    private fun createStraight(cardRank: CardRank): CommonCards {
        val cardRanks = CardRank.values()
        val card1: Card

        // if 5, make the first one an ace for the wheel
        card1 = if (cardRank.ordinal == 3) {
            Card(0, CardRank.ACE, CardSuit.HEARTS)
        } else {
            Card(0, cardRanks[cardRank.ordinal - 4], CardSuit.HEARTS)
        }
        val card2 = Card(0, cardRanks[cardRank.ordinal - 3], CardSuit.CLUBS)
        val card3 = Card(0, cardRanks[cardRank.ordinal - 2], CardSuit.DIAMONDS)
        val card4 = Card(0, cardRanks[cardRank.ordinal - 1], CardSuit.SPADES)
        val card5 = Card(0, cardRank, CardSuit.HEARTS)
        val flopCards = FlopCards(card2, card4, card5)
        val turnCard = TurnCard(card3)
        val riverCard = RiverCard(card1)
        return CommonCards(flopCards, turnCard, riverCard)
    }

    private fun createStraightFlush(cardRank: CardRank): CommonCards {
        val cardRanks = CardRank.values()
        val card1: Card

        // if 5, make the first one an ace for the wheel
        card1 = if (cardRank.ordinal == 3) {
            Card(0, CardRank.ACE, CardSuit.HEARTS)
        } else {
            Card(0, cardRanks[cardRank.ordinal - 4], CardSuit.HEARTS)
        }
        val card2 = Card(0, cardRanks[cardRank.ordinal - 3], CardSuit.HEARTS)
        val card3 = Card(0, cardRanks[cardRank.ordinal - 2], CardSuit.HEARTS)
        val card4 = Card(0, cardRanks[cardRank.ordinal - 1], CardSuit.HEARTS)
        val card5 = Card(0, cardRank, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        return CommonCards(flopCards, turnCard, riverCard)
    }

    private fun createFourOfAKind(cardRank: CardRank): CommonCards {
        val card1 = Card(0, cardRank, CardSuit.HEARTS)
        val card2 = Card(0, cardRank, CardSuit.CLUBS)
        val card3 = Card(0, cardRank, CardSuit.DIAMONDS)
        val card4 = Card(0, cardRank, CardSuit.SPADES)
        val card5: Card
        card5 = if (cardRank === CardRank.EIGHT) {
            Card(0, CardRank.THREE, CardSuit.HEARTS)
        } else {
            Card(0, cardRank, CardSuit.HEARTS)
        }
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        return CommonCards(flopCards, turnCard, riverCard)
    }

    private fun createFullHouse(
        primaryCardRank: CardRank,
        secondaryCardRank: CardRank
    ): CommonCards {
        val card1 = Card(0, primaryCardRank, CardSuit.HEARTS)
        val card2 = Card(0, secondaryCardRank, CardSuit.CLUBS)
        val card3 = Card(0, secondaryCardRank, CardSuit.DIAMONDS)
        val card4 = Card(0, primaryCardRank, CardSuit.SPADES)
        val card5 = Card(0, primaryCardRank, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
        return CommonCards(flopCards, turnCard, riverCard)
    }
}