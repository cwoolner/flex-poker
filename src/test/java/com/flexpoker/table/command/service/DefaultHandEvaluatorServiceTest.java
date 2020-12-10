package com.flexpoker.table.command.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.table.command.HandRanking;
import com.flexpoker.table.command.Card;
import com.flexpoker.table.command.CardRank;
import com.flexpoker.table.command.CardSuit;
import com.flexpoker.table.command.FlopCards;
import com.flexpoker.table.command.PocketCards;
import com.flexpoker.table.command.RiverCard;
import com.flexpoker.table.command.TurnCard;
import com.flexpoker.table.command.aggregate.CommonCards;

public class DefaultHandEvaluatorServiceTest {

    private DefaultHandEvaluatorService bso = new DefaultHandEvaluatorService();

    @Test
    void testDeterminePossibleHands() {
        testDeterminePossibleHandsStraightFlushOnBoard();
        testDeterminePossibleHandsFourOfAKindOnBoard();
        testDeterminePossibleHandsFullHouseOnBoard();
        testDeterminePossibleHandsFlushOnBoard();
        testDeterminePossibleHandsStraightOnBoard();
        testDeterminePossibleHandsThreeOfAKindOnBoard();
        testDeterminePossibleHandsTwoPairOnBoard();
        testDeterminePossibleHandsOnePairOnBoard();

        testDeterminePossibleHandsScenario1();
        testDeterminePossibleHandsScenario2();
        testDeterminePossibleHandsScenario3();
        testDeterminePossibleHandsScenario4();
        testDeterminePossibleHandsScenario5();
    }

    @Test
    void testDetermineHandEvaluation() {
        testDetermineHandEvaluationScenario1();
        testDetermineHandEvaluationScenario2();
        testDetermineHandEvaluationScenario3();
        testDetermineHandEvaluationScenario4();
    }

    private void testDetermineHandEvaluationScenario1() {
        var card1 = new Card(0, CardRank.FOUR, CardSuit.SPADES);
        var card2 = new Card(0, CardRank.JACK, CardSuit.SPADES);
        var card3 = new Card(0, CardRank.EIGHT, CardSuit.HEARTS);
        var card4 = new Card(0, CardRank.FOUR, CardSuit.HEARTS);
        var card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var id3 = UUID.randomUUID();
        var id4 = UUID.randomUUID();
        var id5 = UUID.randomUUID();
        var id6 = UUID.randomUUID();
        var id8 = UUID.randomUUID();

        var pocketCardsList = new ArrayList<PocketCards>();

        var pocketCards1 = new PocketCards(
                new Card(0, CardRank.NINE, CardSuit.HEARTS), new Card(0, CardRank.ACE, CardSuit.CLUBS));
        var pocketCards2 = new PocketCards(
                new Card(0, CardRank.QUEEN, CardSuit.HEARTS), new Card(0, CardRank.ACE, CardSuit.CLUBS));
        var pocketCards3 = new PocketCards(
                new Card(0, CardRank.TWO, CardSuit.HEARTS), new Card(0, CardRank.ACE, CardSuit.HEARTS));
        var pocketCards4 = new PocketCards(
                new Card(0, CardRank.FOUR, CardSuit.CLUBS), new Card(0, CardRank.FOUR, CardSuit.DIAMONDS));
        var pocketCards5 = new PocketCards(
                new Card(0, CardRank.FIVE, CardSuit.CLUBS), new Card(0, CardRank.FOUR, CardSuit.DIAMONDS));
        var pocketCards6 = new PocketCards(
                new Card(0, CardRank.FOUR, CardSuit.CLUBS), new Card(0, CardRank.EIGHT, CardSuit.CLUBS));
        var pocketCards8 = new PocketCards(
                new Card(0, CardRank.QUEEN, CardSuit.CLUBS), new Card(0, CardRank.QUEEN, CardSuit.DIAMONDS));

        pocketCardsList.add(pocketCards1);
        pocketCardsList.add(pocketCards2);
        pocketCardsList.add(pocketCards3);
        pocketCardsList.add(pocketCards4);
        pocketCardsList.add(pocketCards5);
        pocketCardsList.add(pocketCards6);
        pocketCardsList.add(pocketCards8);

        var handEvaluations = bso.determineHandEvaluation(
                flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings);

        var handEvaluation1 = handEvaluations.get(pocketCards1);
        var handEvaluation2 = handEvaluations.get(pocketCards2);
        var handEvaluation3 = handEvaluations.get(pocketCards3);
        var handEvaluation4 = handEvaluations.get(pocketCards4);
        var handEvaluation5 = handEvaluations.get(pocketCards5);
        var handEvaluation6 = handEvaluations.get(pocketCards6);
        var handEvaluation8 = handEvaluations.get(pocketCards8);

        handEvaluation1.setPlayerId(id1);
        handEvaluation2.setPlayerId(id2);
        handEvaluation3.setPlayerId(id3);
        handEvaluation4.setPlayerId(id4);
        handEvaluation5.setPlayerId(id5);
        handEvaluation6.setPlayerId(id6);
        handEvaluation8.setPlayerId(id8);

        assertEquals(HandRanking.ONE_PAIR, handEvaluation1.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation1.getPrimaryCardRank());
        assertEquals(CardRank.ACE, handEvaluation1.getFirstKicker());
        assertEquals(CardRank.QUEEN, handEvaluation1.getSecondKicker());
        assertEquals(CardRank.JACK, handEvaluation1.getThirdKicker());
        assertEquals(id1, handEvaluation1.getPlayerId());

        assertEquals(HandRanking.TWO_PAIR, handEvaluation2.getHandRanking());
        assertEquals(CardRank.QUEEN, handEvaluation2.getPrimaryCardRank());
        assertEquals(CardRank.FOUR, handEvaluation2.getSecondaryCardRank());
        assertEquals(CardRank.ACE, handEvaluation2.getFirstKicker());
        assertEquals(id2, handEvaluation2.getPlayerId());

        assertEquals(HandRanking.FLUSH, handEvaluation3.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation3.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation3.getFirstKicker());
        assertEquals(CardRank.EIGHT, handEvaluation3.getSecondKicker());
        assertEquals(CardRank.FOUR, handEvaluation3.getThirdKicker());
        assertEquals(CardRank.TWO, handEvaluation3.getFourthKicker());
        assertEquals(id3, handEvaluation3.getPlayerId());

        assertEquals(HandRanking.FOUR_OF_A_KIND, handEvaluation4.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation4.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation4.getFirstKicker());
        assertEquals(id4, handEvaluation4.getPlayerId());

        assertEquals(HandRanking.THREE_OF_A_KIND, handEvaluation5.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation5.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation5.getFirstKicker());
        assertEquals(CardRank.JACK, handEvaluation5.getSecondKicker());
        assertEquals(id5, handEvaluation5.getPlayerId());

        assertEquals(HandRanking.FULL_HOUSE, handEvaluation6.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation6.getPrimaryCardRank());
        assertEquals(CardRank.EIGHT, handEvaluation6.getSecondaryCardRank());
        assertEquals(id6, handEvaluation6.getPlayerId());

        assertEquals(HandRanking.FULL_HOUSE, handEvaluation8.getHandRanking());
        assertEquals(CardRank.QUEEN, handEvaluation8.getPrimaryCardRank());
        assertEquals(CardRank.FOUR, handEvaluation8.getSecondaryCardRank());
        assertEquals(id8, handEvaluation8.getPlayerId());
    }

    private void testDetermineHandEvaluationScenario2() {
        var card1 = new Card(0, CardRank.KING, CardSuit.HEARTS);
        var card2 = new Card(0, CardRank.JACK, CardSuit.HEARTS);
        var card3 = new Card(0, CardRank.NINE, CardSuit.HEARTS);
        var card4 = new Card(0, CardRank.TEN, CardSuit.HEARTS);
        var card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var id3 = UUID.randomUUID();

        var pocketCardsList = new ArrayList<PocketCards>();

        var pocketCards1 = new PocketCards(
                new Card(0, CardRank.EIGHT, CardSuit.HEARTS), new Card(0, CardRank.ACE, CardSuit.CLUBS));
        var pocketCards2 = new PocketCards(
                new Card(0, CardRank.QUEEN, CardSuit.SPADES), new Card(0, CardRank.ACE, CardSuit.HEARTS));
        var pocketCards3 = new PocketCards(
                new Card(0, CardRank.QUEEN, CardSuit.SPADES), new Card(0, CardRank.ACE, CardSuit.DIAMONDS));

        pocketCardsList.add(pocketCards1);
        pocketCardsList.add(pocketCards2);
        pocketCardsList.add(pocketCards3);

        var handEvaluations = bso.determineHandEvaluation(
                flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings);

        var handEvaluation1 = handEvaluations.get(pocketCards1);
        var handEvaluation2 = handEvaluations.get(pocketCards2);
        var handEvaluation3 = handEvaluations.get(pocketCards3);

        handEvaluation1.setPlayerId(id1);
        handEvaluation2.setPlayerId(id2);
        handEvaluation3.setPlayerId(id3);

        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation1.getHandRanking());
        assertEquals(CardRank.KING, handEvaluation1.getPrimaryCardRank());
        assertEquals(id1, handEvaluation1.getPlayerId());

        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation2.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation2.getPrimaryCardRank());
        assertEquals(id2, handEvaluation2.getPlayerId());

        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation3.getHandRanking());
        assertEquals(CardRank.KING, handEvaluation3.getPrimaryCardRank());
        assertEquals(id3, handEvaluation3.getPlayerId());
    }

    private void testDetermineHandEvaluationScenario3() {
        var card1 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        var card2 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
        var card3 = new Card(0, CardRank.THREE, CardSuit.CLUBS);
        var card4 = new Card(0, CardRank.FOUR, CardSuit.CLUBS);
        var card5 = new Card(0, CardRank.SIX, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var id3 = UUID.randomUUID();

        var pocketCardsList = new ArrayList<PocketCards>();

        var pocketCards1 = new PocketCards(
                new Card(0, CardRank.FIVE, CardSuit.CLUBS), new Card(0, CardRank.ACE, CardSuit.CLUBS));
        var pocketCards2 = new PocketCards(
                new Card(0, CardRank.FIVE, CardSuit.SPADES), new Card(0, CardRank.ACE, CardSuit.HEARTS));
        var pocketCards3 = new PocketCards(
                new Card(0, CardRank.QUEEN, CardSuit.SPADES), new Card(0, CardRank.ACE, CardSuit.DIAMONDS));

        pocketCardsList.add(pocketCards1);
        pocketCardsList.add(pocketCards2);
        pocketCardsList.add(pocketCards3);

        var handEvaluations = bso.determineHandEvaluation(
                flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings);

        var handEvaluation1 = handEvaluations.get(pocketCards1);
        var handEvaluation2 = handEvaluations.get(pocketCards2);
        var handEvaluation3 = handEvaluations.get(pocketCards3);

        handEvaluation1.setPlayerId(id1);
        handEvaluation2.setPlayerId(id2);
        handEvaluation3.setPlayerId(id3);

        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation1.getHandRanking());
        assertEquals(CardRank.FIVE, handEvaluation1.getPrimaryCardRank());
        assertEquals(id1, handEvaluation1.getPlayerId());

        assertEquals(HandRanking.STRAIGHT, handEvaluation2.getHandRanking());
        assertEquals(CardRank.SIX, handEvaluation2.getPrimaryCardRank());
        assertEquals(id2, handEvaluation2.getPlayerId());

        assertEquals(HandRanking.ONE_PAIR, handEvaluation3.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation3.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation3.getFirstKicker());
        assertEquals(CardRank.SIX, handEvaluation3.getSecondKicker());
        assertEquals(CardRank.FOUR, handEvaluation3.getThirdKicker());
        assertEquals(id3, handEvaluation3.getPlayerId());
    }

    private void testDetermineHandEvaluationScenario4() {
        var card1 = new Card(0, CardRank.KING, CardSuit.SPADES);
        var card2 = new Card(0, CardRank.TWO, CardSuit.DIAMONDS);
        var card3 = new Card(0, CardRank.QUEEN, CardSuit.CLUBS);
        var card4 = new Card(0, CardRank.FOUR, CardSuit.CLUBS);
        var card5 = new Card(0, CardRank.SIX, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var possibleHandRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        var pocketCardsList = new ArrayList<PocketCards>();

        var pocketCards1 = new PocketCards(
                new Card(0, CardRank.FIVE, CardSuit.CLUBS), new Card(0, CardRank.ACE, CardSuit.CLUBS));
        var pocketCards2 = new PocketCards(
                new Card(0, CardRank.THREE, CardSuit.SPADES), new Card(0, CardRank.SEVEN, CardSuit.HEARTS));

        pocketCardsList.add(pocketCards1);
        pocketCardsList.add(pocketCards2);

        var handEvaluations = bso.determineHandEvaluation(
                flopCards, turnCard, riverCard, pocketCardsList, possibleHandRankings);

        var handEvaluation1 = handEvaluations.get(pocketCards1);
        var handEvaluation2 = handEvaluations.get(pocketCards2);

        handEvaluation1.setPlayerId(id1);
        handEvaluation2.setPlayerId(id2);

        assertEquals(HandRanking.HIGH_CARD, handEvaluation1.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation1.getPrimaryCardRank());
        assertEquals(CardRank.KING, handEvaluation1.getFirstKicker());
        assertEquals(CardRank.QUEEN, handEvaluation1.getSecondKicker());
        assertEquals(CardRank.SIX, handEvaluation1.getThirdKicker());
        assertEquals(CardRank.FIVE, handEvaluation1.getFourthKicker());
        assertEquals(id1, handEvaluation1.getPlayerId());

        assertEquals(HandRanking.HIGH_CARD, handEvaluation2.getHandRanking());
        assertEquals(CardRank.KING, handEvaluation2.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation2.getFirstKicker());
        assertEquals(CardRank.SEVEN, handEvaluation2.getSecondKicker());
        assertEquals(CardRank.SIX, handEvaluation2.getThirdKicker());
        assertEquals(CardRank.FOUR, handEvaluation2.getFourthKicker());
        assertEquals(id2, handEvaluation2.getPlayerId());
    }

    private void testDeterminePossibleHandsScenario1() {
        var card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        var card2 = new Card(0, CardRank.TWO, CardSuit.DIAMONDS);
        var card3 = new Card(0, CardRank.SEVEN, CardSuit.CLUBS);
        var card4 = new Card(0, CardRank.EIGHT, CardSuit.SPADES);
        var card5 = new Card(0, CardRank.KING, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(4, handRankings.size());
        assertEquals(HandRanking.HIGH_CARD, handRankings.get(0));
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(1));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(2));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(3));
    }

    private void testDeterminePossibleHandsScenario2() {
        var card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        var card2 = new Card(0, CardRank.KING, CardSuit.HEARTS);
        var card3 = new Card(0, CardRank.SEVEN, CardSuit.CLUBS);
        var card4 = new Card(0, CardRank.EIGHT, CardSuit.SPADES);
        var card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(7, handRankings.size());
        assertEquals(HandRanking.HIGH_CARD, handRankings.get(0));
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(1));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(2));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(3));
        assertEquals(HandRanking.STRAIGHT, handRankings.get(4));
        assertEquals(HandRanking.FLUSH, handRankings.get(5));
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings.get(6));
    }

    private void testDeterminePossibleHandsScenario3() {
        var card1 = new Card(0, CardRank.THREE, CardSuit.CLUBS);
        var card2 = new Card(0, CardRank.NINE, CardSuit.SPADES);
        var card3 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
        var card4 = new Card(0, CardRank.SIX, CardSuit.CLUBS);
        var card5 = new Card(0, CardRank.THREE, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(8, handRankings.size());
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(0));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(1));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(2));
        assertEquals(HandRanking.STRAIGHT, handRankings.get(3));
        assertEquals(HandRanking.FLUSH, handRankings.get(4));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(5));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(6));
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings.get(7));
    }

    private void testDeterminePossibleHandsScenario4() {
        var card1 = new Card(0, CardRank.TEN, CardSuit.CLUBS);
        var card2 = new Card(0, CardRank.SIX, CardSuit.HEARTS);
        var card3 = new Card(0, CardRank.JACK, CardSuit.DIAMONDS);
        var card4 = new Card(0, CardRank.NINE, CardSuit.CLUBS);
        var card5 = new Card(0, CardRank.THREE, CardSuit.CLUBS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(6, handRankings.size());
        assertEquals(HandRanking.HIGH_CARD, handRankings.get(0));
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(1));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(2));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(3));
        assertEquals(HandRanking.STRAIGHT, handRankings.get(4));
        assertEquals(HandRanking.FLUSH, handRankings.get(5));
    }

    private void testDeterminePossibleHandsScenario5() {
        var card1 = new Card(0, CardRank.EIGHT, CardSuit.CLUBS);
        var card2 = new Card(0, CardRank.FOUR, CardSuit.DIAMONDS);
        var card3 = new Card(0, CardRank.ACE, CardSuit.DIAMONDS);
        var card4 = new Card(0, CardRank.EIGHT, CardSuit.DIAMONDS);
        var card5 = new Card(0, CardRank.FIVE, CardSuit.DIAMONDS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        var handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(8, handRankings.size());
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(0));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(1));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(2));
        assertEquals(HandRanking.STRAIGHT, handRankings.get(3));
        assertEquals(HandRanking.FLUSH, handRankings.get(4));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(5));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(6));
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings.get(7));
    }

    private void testDeterminePossibleHandsStraightFlushOnBoard() {
        verifyStraightFlushOnBoardHelper(CardRank.FIVE);
        verifyStraightFlushOnBoardHelper(CardRank.SIX);
        verifyStraightFlushOnBoardHelper(CardRank.SEVEN);
        verifyStraightFlushOnBoardHelper(CardRank.EIGHT);
        verifyStraightFlushOnBoardHelper(CardRank.NINE);
        verifyStraightFlushOnBoardHelper(CardRank.TEN);
        verifyStraightFlushOnBoardHelper(CardRank.JACK);
        verifyStraightFlushOnBoardHelper(CardRank.QUEEN);
        verifyStraightFlushOnBoardHelper(CardRank.KING);
        verifyStraightFlushOnBoardHelper(CardRank.ACE);
    }

    private void testDeterminePossibleHandsFourOfAKindOnBoard() {
        verifyFourOfAKindOnBoardHelper(CardRank.TWO);
        verifyFourOfAKindOnBoardHelper(CardRank.THREE);
        verifyFourOfAKindOnBoardHelper(CardRank.FOUR);
        verifyFourOfAKindOnBoardHelper(CardRank.FIVE);
        verifyStraightFlushOnBoardHelper(CardRank.SIX);
        verifyFourOfAKindOnBoardHelper(CardRank.SEVEN);
        verifyFourOfAKindOnBoardHelper(CardRank.EIGHT);
        verifyStraightFlushOnBoardHelper(CardRank.NINE);
        verifyFourOfAKindOnBoardHelper(CardRank.TEN);
        verifyFourOfAKindOnBoardHelper(CardRank.JACK);
        verifyFourOfAKindOnBoardHelper(CardRank.QUEEN);
        verifyStraightFlushOnBoardHelper(CardRank.KING);
        verifyFourOfAKindOnBoardHelper(CardRank.ACE);
    }

    private void testDeterminePossibleHandsFullHouseOnBoard() {
        verifyFullHouseOnBoardHelper(CardRank.TWO, CardRank.ACE);
        verifyFullHouseOnBoardHelper(CardRank.ACE, CardRank.TWO);
        verifyFullHouseOnBoardHelper(CardRank.KING, CardRank.ACE);
        verifyFullHouseOnBoardHelper(CardRank.ACE, CardRank.KING);
        verifyFullHouseOnBoardHelper(CardRank.KING, CardRank.QUEEN);
        verifyFullHouseOnBoardHelper(CardRank.EIGHT, CardRank.SEVEN);
    }

    private void testDeterminePossibleHandsFlushOnBoard() {
        var card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        var card2 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        var card3 = new Card(0, CardRank.SIX, CardSuit.HEARTS);
        var card4 = new Card(0, CardRank.SEVEN, CardSuit.HEARTS);
        var card5 = new Card(0, CardRank.KING, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card2, card3);
        var turnCard = new TurnCard(card4);
        var riverCard = new RiverCard(card5);

        var handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.FLUSH, handRankings.get(0));

        card1 = new Card(0, CardRank.THREE, CardSuit.SPADES);
        card2 = new Card(0, CardRank.TWO, CardSuit.SPADES);
        card3 = new Card(0, CardRank.EIGHT, CardSuit.SPADES);
        card4 = new Card(0, CardRank.NINE, CardSuit.SPADES);
        card5 = new Card(0, CardRank.KING, CardSuit.SPADES);

        flopCards = new FlopCards(card1, card2, card3);
        turnCard = new TurnCard(card4);
        riverCard = new RiverCard(card5);

        handRankings = bso.determinePossibleHands(flopCards, turnCard, riverCard);
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.FLUSH, handRankings.get(0));
    }

    private void testDeterminePossibleHandsStraightOnBoard() {
        verifyStraightOnBoardHelper(CardRank.FIVE);
        verifyStraightOnBoardHelper(CardRank.SIX);
        verifyStraightOnBoardHelper(CardRank.SEVEN);
        verifyStraightOnBoardHelper(CardRank.EIGHT);
        verifyStraightOnBoardHelper(CardRank.NINE);
        verifyStraightOnBoardHelper(CardRank.TEN);
        verifyStraightOnBoardHelper(CardRank.JACK);
        verifyStraightOnBoardHelper(CardRank.QUEEN);
        verifyStraightOnBoardHelper(CardRank.KING);
        verifyStraightOnBoardHelper(CardRank.ACE);
    }

    private void testDeterminePossibleHandsThreeOfAKindOnBoard() {
        verifyThreeOfAKindOnBoardHelper(CardRank.TWO);
        verifyThreeOfAKindOnBoardHelper(CardRank.THREE);
        verifyThreeOfAKindOnBoardHelper(CardRank.FOUR);
        verifyThreeOfAKindOnBoardHelper(CardRank.FIVE);
        verifyThreeOfAKindOnBoardHelper(CardRank.SIX);
        verifyThreeOfAKindOnBoardHelper(CardRank.SEVEN);
        verifyThreeOfAKindOnBoardHelper(CardRank.EIGHT);
        verifyThreeOfAKindOnBoardHelper(CardRank.NINE);
        verifyThreeOfAKindOnBoardHelper(CardRank.TEN);
        verifyThreeOfAKindOnBoardHelper(CardRank.JACK);
        verifyThreeOfAKindOnBoardHelper(CardRank.QUEEN);
        verifyThreeOfAKindOnBoardHelper(CardRank.KING);
        verifyThreeOfAKindOnBoardHelper(CardRank.ACE);
    }

    private void testDeterminePossibleHandsTwoPairOnBoard() {
        verifyTwoPairOnBoardHelper(CardRank.TWO, CardRank.KING);
        verifyTwoPairOnBoardHelper(CardRank.THREE, CardRank.ACE);
        verifyTwoPairOnBoardHelper(CardRank.FOUR, CardRank.SEVEN);
        verifyTwoPairOnBoardHelper(CardRank.FIVE, CardRank.EIGHT);
        verifyTwoPairOnBoardHelper(CardRank.SIX, CardRank.ACE);
        verifyTwoPairOnBoardHelper(CardRank.SEVEN, CardRank.TWO);
        verifyTwoPairOnBoardHelper(CardRank.EIGHT, CardRank.NINE);
        verifyTwoPairOnBoardHelper(CardRank.NINE, CardRank.TEN);
        verifyTwoPairOnBoardHelper(CardRank.TEN, CardRank.JACK);
        verifyTwoPairOnBoardHelper(CardRank.JACK, CardRank.THREE);
        verifyTwoPairOnBoardHelper(CardRank.QUEEN, CardRank.KING);
        verifyTwoPairOnBoardHelper(CardRank.KING, CardRank.SIX);
        verifyTwoPairOnBoardHelper(CardRank.ACE, CardRank.KING);
    }

    private void testDeterminePossibleHandsOnePairOnBoard() {
        verifyOnePairOnBoardHelper(CardRank.TWO);
        verifyOnePairOnBoardHelper(CardRank.THREE);
        verifyOnePairOnBoardHelper(CardRank.FOUR);
        verifyOnePairOnBoardHelper(CardRank.FIVE);
        verifyOnePairOnBoardHelper(CardRank.SIX);
        verifyOnePairOnBoardHelper(CardRank.SEVEN);
        verifyOnePairOnBoardHelper(CardRank.EIGHT);
        verifyOnePairOnBoardHelper(CardRank.NINE);
        verifyOnePairOnBoardHelper(CardRank.TEN);
        verifyOnePairOnBoardHelper(CardRank.JACK);
        verifyOnePairOnBoardHelper(CardRank.QUEEN);
        verifyOnePairOnBoardHelper(CardRank.KING);
        verifyOnePairOnBoardHelper(CardRank.ACE);
    }

    private void verifyStraightFlushOnBoardHelper(CardRank cardRank) {
        var commonCards = createStraightFlush(cardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings.get(0));
    }

    private void verifyFourOfAKindOnBoardHelper(CardRank cardRank) {
        var commonCards = createFourOfAKind(cardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(0));
    }

    private void verifyFullHouseOnBoardHelper(CardRank primaryCardRank, CardRank secondaryCardRank) {
        var commonCards = createFullHouse(primaryCardRank, secondaryCardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(2, handRankings.size());
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(0));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(1));
    }

    private void verifyStraightOnBoardHelper(CardRank cardRank) {
        var commonCards = createStraight(cardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.STRAIGHT, handRankings.get(0));
    }

    private void verifyThreeOfAKindOnBoardHelper(CardRank cardRank) {
        var commonCards = createThreeOfAKind(cardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(3, handRankings.size());
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(0));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(1));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(2));
    }

    private void verifyTwoPairOnBoardHelper(CardRank primaryCardRank, CardRank secondaryCardRank) {
        var commonCards = createTwoPair(primaryCardRank, secondaryCardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(3, handRankings.size());
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(0));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(1));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(2));
    }

    private void verifyOnePairOnBoardHelper(CardRank cardRank) {
        var commonCards = createOnePair(cardRank);
        var handRankings = bso.determinePossibleHands(
                commonCards.getFlopCards(), commonCards.getTurnCard(), commonCards.getRiverCard());
        assertEquals(5, handRankings.size());
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(0));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(1));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(2));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(3));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(4));
    }

    private CommonCards createThreeOfAKind(CardRank cardRank) {
        var card1 = new Card(0, cardRank, CardSuit.CLUBS);
        var card4 = new Card(0, cardRank, CardSuit.SPADES);
        var card2 = new Card(0, cardRank, CardSuit.DIAMONDS);

        Card card3;
        Card card5;

        if (cardRank.ordinal() < 7) {
            card3 = new Card(0, CardRank.KING, CardSuit.HEARTS);
            card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
        } else {
            card3 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
            card5 = new Card(0, CardRank.SIX, CardSuit.HEARTS);
        }

        var flopCards = new FlopCards(card2, card4, card5);
        var turnCard = new TurnCard(card3);
        var riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createTwoPair(CardRank primaryCardRank, CardRank secondaryCardRank) {
        var card1 = new Card(0, primaryCardRank, CardSuit.CLUBS);
        var card4 = new Card(0, primaryCardRank, CardSuit.SPADES);
        var card2 = new Card(0, secondaryCardRank, CardSuit.HEARTS);
        var card3 = new Card(0, secondaryCardRank, CardSuit.SPADES);

        Card card5;

        if (primaryCardRank.ordinal() < 7) {
            if (secondaryCardRank.equals(CardRank.KING)) {
                card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
            } else {
                card5 = new Card(0, CardRank.KING, CardSuit.HEARTS);
            }
        } else {
            if (secondaryCardRank.equals(CardRank.THREE)) {
                card5 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
            } else {
                card5 = new Card(0, CardRank.THREE, CardSuit.HEARTS);
            }
        }

        var flopCards = new FlopCards(card2, card4, card5);
        var turnCard = new TurnCard(card3);
        var riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createOnePair(CardRank cardRank) {
        var card1 = new Card(0, cardRank, CardSuit.CLUBS);
        var card4 = new Card(0, cardRank, CardSuit.SPADES);

        Card card2;
        Card card3;
        Card card5;

        if (cardRank.equals(CardRank.TWO)) {
            card2 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
            card3 = new Card(0, CardRank.SEVEN, CardSuit.DIAMONDS);
            card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
        } else if (cardRank.equals(CardRank.SEVEN)) {
            card2 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
            card3 = new Card(0, CardRank.ACE, CardSuit.DIAMONDS);
            card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
        } else if (cardRank.equals(CardRank.QUEEN)) {
            card2 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
            card3 = new Card(0, CardRank.SEVEN, CardSuit.DIAMONDS);
            card5 = new Card(0, CardRank.KING, CardSuit.HEARTS);
        } else {
            card2 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
            card3 = new Card(0, CardRank.SEVEN, CardSuit.DIAMONDS);
            card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
        }

        var flopCards = new FlopCards(card2, card4, card5);
        var turnCard = new TurnCard(card3);
        var riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createStraight(CardRank cardRank) {
        var cardRanks = CardRank.values();

        Card card1;

        // if 5, make the first one an ace for the wheel
        if (cardRank.ordinal() == 3) {
            card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        } else {
            card1 = new Card(0, cardRanks[cardRank.ordinal() - 4], CardSuit.HEARTS);
        }

        var card2 = new Card(0, cardRanks[cardRank.ordinal() - 3], CardSuit.CLUBS);
        var card3 = new Card(0, cardRanks[cardRank.ordinal() - 2], CardSuit.DIAMONDS);
        var card4 = new Card(0, cardRanks[cardRank.ordinal() - 1], CardSuit.SPADES);
        var card5 = new Card(0, cardRank, CardSuit.HEARTS);

        var flopCards = new FlopCards(card2, card4, card5);
        var turnCard = new TurnCard(card3);
        var riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createStraightFlush(CardRank cardRank) {
        var cardRanks = CardRank.values();

        Card card1;

        // if 5, make the first one an ace for the wheel
        if (cardRank.ordinal() == 3) {
            card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        } else {
            card1 = new Card(0, cardRanks[cardRank.ordinal() - 4], CardSuit.HEARTS);
        }

        var card2 = new Card(0, cardRanks[cardRank.ordinal() - 3], CardSuit.HEARTS);
        var card3 = new Card(0, cardRanks[cardRank.ordinal() - 2], CardSuit.HEARTS);
        var card4 = new Card(0, cardRanks[cardRank.ordinal() - 1], CardSuit.HEARTS);
        var card5 = new Card(0, cardRank, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createFourOfAKind(CardRank cardRank) {
        var card1 = new Card(0, cardRank, CardSuit.HEARTS);
        var card2 = new Card(0, cardRank, CardSuit.CLUBS);
        var card3 = new Card(0, cardRank, CardSuit.DIAMONDS);
        var card4 = new Card(0, cardRank, CardSuit.SPADES);
        Card card5;

        if (cardRank == CardRank.EIGHT) {
            card5 = new Card(0, CardRank.THREE, CardSuit.HEARTS);
        } else {
            card5 = new Card(0, cardRank, CardSuit.HEARTS);
        }

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createFullHouse(CardRank primaryCardRank,
            CardRank secondaryCardRank) {
        var card1 = new Card(0, primaryCardRank, CardSuit.HEARTS);
        var card2 = new Card(0, secondaryCardRank, CardSuit.CLUBS);
        var card3 = new Card(0, secondaryCardRank, CardSuit.DIAMONDS);
        var card4 = new Card(0, primaryCardRank, CardSuit.SPADES);
        var card5 = new Card(0, primaryCardRank, CardSuit.HEARTS);

        var flopCards = new FlopCards(card1, card3, card4);
        var turnCard = new TurnCard(card5);
        var riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

}
