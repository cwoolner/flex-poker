package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.HandRanking
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class DefaultHandEvaluatorServiceTest {

    private val bso = DefaultHandEvaluatorService()

    @Test
    fun testDetermineHandEvaluationScenario1() {
        val card1 = Card(0, CardRank.FOUR, CardSuit.SPADES)
        val card2 = Card(0, CardRank.JACK, CardSuit.SPADES)
        val card3 = Card(0, CardRank.EIGHT, CardSuit.HEARTS)
        val card4 = Card(0, CardRank.FOUR, CardSuit.HEARTS)
        val card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
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
        val handEvaluations = bso.determineHandEvaluation(flopCards, turnCard, riverCard, pocketCardsList)
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

    @Test
    fun testDetermineHandEvaluationScenario2() {
        val card1 = Card(0, CardRank.KING, CardSuit.HEARTS)
        val card2 = Card(0, CardRank.JACK, CardSuit.HEARTS)
        val card3 = Card(0, CardRank.NINE, CardSuit.HEARTS)
        val card4 = Card(0, CardRank.TEN, CardSuit.HEARTS)
        val card5 = Card(0, CardRank.QUEEN, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
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
        val handEvaluations = bso.determineHandEvaluation(flopCards, turnCard, riverCard, pocketCardsList)
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

    @Test
    fun testDetermineHandEvaluationScenario3() {
        val card1 = Card(0, CardRank.ACE, CardSuit.CLUBS)
        val card2 = Card(0, CardRank.TWO, CardSuit.CLUBS)
        val card3 = Card(0, CardRank.THREE, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.FOUR, CardSuit.CLUBS)
        val card5 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
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
        val handEvaluations = bso.determineHandEvaluation(flopCards, turnCard, riverCard, pocketCardsList)
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

    @Test
    fun testDetermineHandEvaluationScenario4() {
        val card1 = Card(0, CardRank.KING, CardSuit.SPADES)
        val card2 = Card(0, CardRank.TWO, CardSuit.DIAMONDS)
        val card3 = Card(0, CardRank.QUEEN, CardSuit.CLUBS)
        val card4 = Card(0, CardRank.FOUR, CardSuit.CLUBS)
        val card5 = Card(0, CardRank.SIX, CardSuit.HEARTS)
        val flopCards = FlopCards(card1, card3, card4)
        val turnCard = TurnCard(card5)
        val riverCard = RiverCard(card2)
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
        val handEvaluations = bso.determineHandEvaluation(flopCards, turnCard, riverCard, pocketCardsList)
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

}