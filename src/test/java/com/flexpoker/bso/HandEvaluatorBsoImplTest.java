package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.flexpoker.model.Card;
import com.flexpoker.model.CardRank;
import com.flexpoker.model.CardSuit;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

public class HandEvaluatorBsoImplTest {

    private HandEvaluatorBsoImpl bso = new HandEvaluatorBsoImpl();

    @Test
    public void testDeterminePossibleHands() {
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
    public void testDetermineHandEvaluation() {
        testDetermineHandEvaluationScenario1();
        testDetermineHandEvaluationScenario2();
        testDetermineHandEvaluationScenario3();
        testDetermineHandEvaluationScenario4();
    }

    private void testDetermineHandEvaluationScenario1() {
        Card card1 = new Card(0, CardRank.FOUR, CardSuit.SPADES);
        Card card2 = new Card(0, CardRank.JACK, CardSuit.SPADES);
        Card card3 = new Card(0, CardRank.EIGHT, CardSuit.HEARTS);
        Card card4 = new Card(0, CardRank.FOUR, CardSuit.HEARTS);
        Card card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> possibleHandRankings = bso.determinePossibleHands(commonCards);

        User user = new User();
        user.setId(1);
        Card pocketCard1 = new Card(0, CardRank.NINE, CardSuit.HEARTS);
        Card pocketCard2 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        PocketCards pocketCards = new PocketCards(pocketCard1, pocketCard2);

        HandEvaluation handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.ONE_PAIR, handEvaluation.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.ACE, handEvaluation.getFirstKicker());
        assertEquals(CardRank.QUEEN, handEvaluation.getSecondKicker());
        assertEquals(CardRank.JACK, handEvaluation.getThirdKicker());
        assertEquals(1, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(2);
        pocketCard1 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
        pocketCard2 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.TWO_PAIR, handEvaluation.getHandRanking());
        assertEquals(CardRank.QUEEN, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.FOUR, handEvaluation.getSecondaryCardRank());
        assertEquals(CardRank.ACE, handEvaluation.getFirstKicker());
        assertEquals(2, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(3);
        pocketCard1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        pocketCard2 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.FLUSH, handEvaluation.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation.getFirstKicker());
        assertEquals(CardRank.EIGHT, handEvaluation.getSecondKicker());
        assertEquals(CardRank.FOUR, handEvaluation.getThirdKicker());
        assertEquals(CardRank.TWO, handEvaluation.getFourthKicker());
        assertEquals(3, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(4);
        pocketCard1 = new Card(0, CardRank.FOUR, CardSuit.CLUBS);
        pocketCard2 = new Card(0, CardRank.FOUR, CardSuit.DIAMONDS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.FOUR_OF_A_KIND, handEvaluation.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation.getFirstKicker());
        assertEquals(4, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(5);
        pocketCard1 = new Card(0, CardRank.FIVE, CardSuit.CLUBS);
        pocketCard2 = new Card(0, CardRank.FOUR, CardSuit.DIAMONDS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.THREE_OF_A_KIND, handEvaluation.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation.getFirstKicker());
        assertEquals(CardRank.JACK, handEvaluation.getSecondKicker());
        assertEquals(5, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(6);
        pocketCard1 = new Card(0, CardRank.FOUR, CardSuit.CLUBS);
        pocketCard2 = new Card(0, CardRank.EIGHT, CardSuit.CLUBS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.FULL_HOUSE, handEvaluation.getHandRanking());
        assertEquals(CardRank.FOUR, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.EIGHT, handEvaluation.getSecondaryCardRank());
        assertEquals(6, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(8);
        pocketCard1 = new Card(0, CardRank.QUEEN, CardSuit.CLUBS);
        pocketCard2 = new Card(0, CardRank.QUEEN, CardSuit.DIAMONDS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.FULL_HOUSE, handEvaluation.getHandRanking());
        assertEquals(CardRank.QUEEN, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.FOUR, handEvaluation.getSecondaryCardRank());
        assertEquals(8, handEvaluation.getUser().getId().intValue());
    }

    private void testDetermineHandEvaluationScenario2() {
        Card card1 = new Card(0, CardRank.KING, CardSuit.HEARTS);
        Card card2 = new Card(0, CardRank.JACK, CardSuit.HEARTS);
        Card card3 = new Card(0, CardRank.NINE, CardSuit.HEARTS);
        Card card4 = new Card(0, CardRank.TEN, CardSuit.HEARTS);
        Card card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> possibleHandRankings = bso.determinePossibleHands(commonCards);

        User user = new User();
        user.setId(1);
        Card pocketCard1 = new Card(0, CardRank.EIGHT, CardSuit.HEARTS);
        Card pocketCard2 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        PocketCards pocketCards = new PocketCards(pocketCard1, pocketCard2);

        HandEvaluation handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation.getHandRanking());
        assertEquals(CardRank.KING, handEvaluation.getPrimaryCardRank());
        assertEquals(1, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(2);
        pocketCard1 = new Card(0, CardRank.QUEEN, CardSuit.SPADES);
        pocketCard2 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation.getPrimaryCardRank());
        assertEquals(2, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(3);
        pocketCard1 = new Card(0, CardRank.QUEEN, CardSuit.SPADES);
        pocketCard2 = new Card(0, CardRank.ACE, CardSuit.DIAMONDS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation.getHandRanking());
        assertEquals(CardRank.KING, handEvaluation.getPrimaryCardRank());
        assertEquals(3, handEvaluation.getUser().getId().intValue());
    }

    private void testDetermineHandEvaluationScenario3() {
        Card card1 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        Card card2 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
        Card card3 = new Card(0, CardRank.THREE, CardSuit.CLUBS);
        Card card4 = new Card(0, CardRank.FOUR, CardSuit.CLUBS);
        Card card5 = new Card(0, CardRank.SIX, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> possibleHandRankings = bso.determinePossibleHands(commonCards);

        User user = new User();
        user.setId(1);
        Card pocketCard1 = new Card(0, CardRank.FIVE, CardSuit.CLUBS);
        Card pocketCard2 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        PocketCards pocketCards = new PocketCards(pocketCard1, pocketCard2);

        HandEvaluation handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.STRAIGHT_FLUSH, handEvaluation.getHandRanking());
        assertEquals(CardRank.FIVE, handEvaluation.getPrimaryCardRank());
        assertEquals(1, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(2);
        pocketCard1 = new Card(0, CardRank.FIVE, CardSuit.SPADES);
        pocketCard2 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.STRAIGHT, handEvaluation.getHandRanking());
        assertEquals(CardRank.SIX, handEvaluation.getPrimaryCardRank());
        assertEquals(2, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(3);
        pocketCard1 = new Card(0, CardRank.QUEEN, CardSuit.SPADES);
        pocketCard2 = new Card(0, CardRank.ACE, CardSuit.DIAMONDS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.ONE_PAIR, handEvaluation.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation.getFirstKicker());
        assertEquals(CardRank.SIX, handEvaluation.getSecondKicker());
        assertEquals(CardRank.FOUR, handEvaluation.getThirdKicker());
        assertEquals(3, handEvaluation.getUser().getId().intValue());
    }

    private void testDetermineHandEvaluationScenario4() {
        Card card1 = new Card(0, CardRank.KING, CardSuit.SPADES);
        Card card2 = new Card(0, CardRank.TWO, CardSuit.DIAMONDS);
        Card card3 = new Card(0, CardRank.QUEEN, CardSuit.CLUBS);
        Card card4 = new Card(0, CardRank.FOUR, CardSuit.CLUBS);
        Card card5 = new Card(0, CardRank.SIX, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> possibleHandRankings = bso.determinePossibleHands(commonCards);

        User user = new User();
        user.setId(1);
        Card pocketCard1 = new Card(0, CardRank.FIVE, CardSuit.CLUBS);
        Card pocketCard2 = new Card(0, CardRank.ACE, CardSuit.CLUBS);
        PocketCards pocketCards = new PocketCards(pocketCard1, pocketCard2);

        HandEvaluation handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.HIGH_CARD, handEvaluation.getHandRanking());
        assertEquals(CardRank.ACE, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.KING, handEvaluation.getFirstKicker());
        assertEquals(CardRank.QUEEN, handEvaluation.getSecondKicker());
        assertEquals(CardRank.SIX, handEvaluation.getThirdKicker());
        assertEquals(CardRank.FIVE, handEvaluation.getFourthKicker());
        assertEquals(1, handEvaluation.getUser().getId().intValue());

        user = new User();
        user.setId(2);
        pocketCard1 = new Card(0, CardRank.THREE, CardSuit.SPADES);
        pocketCard2 = new Card(0, CardRank.SEVEN, CardSuit.HEARTS);
        pocketCards = new PocketCards(pocketCard1, pocketCard2);

        handEvaluation = bso.determineHandEvaluation(commonCards,
                user, pocketCards, possibleHandRankings);
        assertEquals(HandRanking.HIGH_CARD, handEvaluation.getHandRanking());
        assertEquals(CardRank.KING, handEvaluation.getPrimaryCardRank());
        assertEquals(CardRank.QUEEN, handEvaluation.getFirstKicker());
        assertEquals(CardRank.SEVEN, handEvaluation.getSecondKicker());
        assertEquals(CardRank.SIX, handEvaluation.getThirdKicker());
        assertEquals(CardRank.FOUR, handEvaluation.getFourthKicker());
        assertEquals(2, handEvaluation.getUser().getId().intValue());
    }

    private void testDeterminePossibleHandsScenario1() {
        Card card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        Card card2 = new Card(0, CardRank.TWO, CardSuit.DIAMONDS);
        Card card3 = new Card(0, CardRank.SEVEN, CardSuit.CLUBS);
        Card card4 = new Card(0, CardRank.EIGHT, CardSuit.SPADES);
        Card card5 = new Card(0, CardRank.KING, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(4, handRankings.size());
        assertEquals(HandRanking.HIGH_CARD, handRankings.get(0));
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(1));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(2));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(3));
    }

    private void testDeterminePossibleHandsScenario2() {
        Card card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        Card card2 = new Card(0, CardRank.KING, CardSuit.HEARTS);
        Card card3 = new Card(0, CardRank.SEVEN, CardSuit.CLUBS);
        Card card4 = new Card(0, CardRank.EIGHT, CardSuit.SPADES);
        Card card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
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
        Card card1 = new Card(0, CardRank.THREE, CardSuit.CLUBS);
        Card card2 = new Card(0, CardRank.NINE, CardSuit.SPADES);
        Card card3 = new Card(0, CardRank.TWO, CardSuit.CLUBS);
        Card card4 = new Card(0, CardRank.SIX, CardSuit.CLUBS);
        Card card5 = new Card(0, CardRank.THREE, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
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
        Card card1 = new Card(0, CardRank.TEN, CardSuit.CLUBS);
        Card card2 = new Card(0, CardRank.SIX, CardSuit.HEARTS);
        Card card3 = new Card(0, CardRank.JACK, CardSuit.DIAMONDS);
        Card card4 = new Card(0, CardRank.NINE, CardSuit.CLUBS);
        Card card5 = new Card(0, CardRank.THREE, CardSuit.CLUBS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(6, handRankings.size());
        assertEquals(HandRanking.HIGH_CARD, handRankings.get(0));
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(1));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(2));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(3));
        assertEquals(HandRanking.STRAIGHT, handRankings.get(4));
        assertEquals(HandRanking.FLUSH, handRankings.get(5));
    }

    private void testDeterminePossibleHandsScenario5() {
        Card card1 = new Card(0, CardRank.EIGHT, CardSuit.CLUBS);
        Card card2 = new Card(0, CardRank.FOUR, CardSuit.DIAMONDS);
        Card card3 = new Card(0, CardRank.ACE, CardSuit.DIAMONDS);
        Card card4 = new Card(0, CardRank.EIGHT, CardSuit.DIAMONDS);
        Card card5 = new Card(0, CardRank.FIVE, CardSuit.DIAMONDS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
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
        Card card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        Card card2 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        Card card3 = new Card(0, CardRank.SIX, CardSuit.HEARTS);
        Card card4 = new Card(0, CardRank.SEVEN, CardSuit.HEARTS);
        Card card5 = new Card(0, CardRank.KING, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card2, card3);
        TurnCard turnCard = new TurnCard(card4);
        RiverCard riverCard = new RiverCard(card5);
        CommonCards commonCards = new CommonCards(flopCards, turnCard, riverCard);

        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
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
        commonCards = new CommonCards(flopCards, turnCard, riverCard);

        handRankings = bso.determinePossibleHands(commonCards);
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
        CommonCards commonCards = createStraightFlush(cardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.STRAIGHT_FLUSH, handRankings.get(0));
    }

    private void verifyFourOfAKindOnBoardHelper(CardRank cardRank) {
        CommonCards commonCards = createFourOfAKind(cardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(0));
    }

    private void verifyFullHouseOnBoardHelper(CardRank primaryCardRank,
            CardRank secondaryCardRank) {
        CommonCards commonCards = createFullHouse(primaryCardRank, secondaryCardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(2, handRankings.size());
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(0));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(1));
    }

    private void verifyStraightOnBoardHelper(CardRank cardRank) {
        CommonCards commonCards = createStraight(cardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.STRAIGHT, handRankings.get(0));
    }

    private void verifyThreeOfAKindOnBoardHelper(CardRank cardRank) {
        CommonCards commonCards = createThreeOfAKind(cardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(3, handRankings.size());
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(0));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(1));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(2));
    }

    private void verifyTwoPairOnBoardHelper(CardRank primaryCardRank,
            CardRank secondaryCardRank) {
        CommonCards commonCards = createTwoPair(primaryCardRank, secondaryCardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(3, handRankings.size());
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(0));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(1));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(2));
    }

    private void verifyOnePairOnBoardHelper(CardRank cardRank) {
        CommonCards commonCards = createOnePair(cardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(5, handRankings.size());
        assertEquals(HandRanking.ONE_PAIR, handRankings.get(0));
        assertEquals(HandRanking.TWO_PAIR, handRankings.get(1));
        assertEquals(HandRanking.THREE_OF_A_KIND, handRankings.get(2));
        assertEquals(HandRanking.FULL_HOUSE, handRankings.get(3));
        assertEquals(HandRanking.FOUR_OF_A_KIND, handRankings.get(4));
    }

    private CommonCards createThreeOfAKind(CardRank cardRank) {
        Card card1 = new Card(0, cardRank, CardSuit.CLUBS);
        Card card4 = new Card(0, cardRank, CardSuit.SPADES);
        Card card2 = new Card(0, cardRank, CardSuit.DIAMONDS);

        Card card3;
        Card card5;

        if (cardRank.ordinal() < 7) {
            card3 = new Card(0, CardRank.KING, CardSuit.HEARTS);
            card5 = new Card(0, CardRank.QUEEN, CardSuit.HEARTS);
        } else {
            card3 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
            card5 = new Card(0, CardRank.SIX, CardSuit.HEARTS);
        }

        FlopCards flopCards = new FlopCards(card2, card4, card5);
        TurnCard turnCard = new TurnCard(card3);
        RiverCard riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createTwoPair(CardRank primaryCardRank,
            CardRank secondaryCardRank) {
        Card card1 = new Card(0, primaryCardRank, CardSuit.CLUBS);
        Card card4 = new Card(0, primaryCardRank, CardSuit.SPADES);
        Card card2 = new Card(0, secondaryCardRank, CardSuit.HEARTS);
        Card card3 = new Card(0, secondaryCardRank, CardSuit.SPADES);

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

        FlopCards flopCards = new FlopCards(card2, card4, card5);
        TurnCard turnCard = new TurnCard(card3);
        RiverCard riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createOnePair(CardRank cardRank) {
        Card card1 = new Card(0, cardRank, CardSuit.CLUBS);
        Card card4 = new Card(0, cardRank, CardSuit.SPADES);

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

        FlopCards flopCards = new FlopCards(card2, card4, card5);
        TurnCard turnCard = new TurnCard(card3);
        RiverCard riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createStraight(CardRank cardRank) {
        CardRank[] cardRanks = CardRank.values();

        Card card1;

        // if 5, make the first one an ace for the wheel
        if (cardRank.ordinal() == 3) {
            card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        } else {
            card1 = new Card(0, cardRanks[cardRank.ordinal() - 4], CardSuit.HEARTS);
        }

        Card card2 = new Card(0, cardRanks[cardRank.ordinal() - 3], CardSuit.CLUBS);
        Card card3 = new Card(0, cardRanks[cardRank.ordinal() - 2], CardSuit.DIAMONDS);
        Card card4 = new Card(0, cardRanks[cardRank.ordinal() - 1], CardSuit.SPADES);
        Card card5 = new Card(0, cardRank, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card2, card4, card5);
        TurnCard turnCard = new TurnCard(card3);
        RiverCard riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createStraightFlush(CardRank cardRank) {
        CardRank[] cardRanks = CardRank.values();

        Card card1;

        // if 5, make the first one an ace for the wheel
        if (cardRank.ordinal() == 3) {
            card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        } else {
            card1 = new Card(0, cardRanks[cardRank.ordinal() - 4], CardSuit.HEARTS);
        }

        Card card2 = new Card(0, cardRanks[cardRank.ordinal() - 3], CardSuit.HEARTS);
        Card card3 = new Card(0, cardRanks[cardRank.ordinal() - 2], CardSuit.HEARTS);
        Card card4 = new Card(0, cardRanks[cardRank.ordinal() - 1], CardSuit.HEARTS);
        Card card5 = new Card(0, cardRank, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createFourOfAKind(CardRank cardRank) {
        Card card1 = new Card(0, cardRank, CardSuit.HEARTS);
        Card card2 = new Card(0, cardRank, CardSuit.CLUBS);
        Card card3 = new Card(0, cardRank, CardSuit.DIAMONDS);
        Card card4 = new Card(0, cardRank, CardSuit.SPADES);
        Card card5;

        if (cardRank == CardRank.EIGHT) {
            card5 = new Card(0, CardRank.THREE, CardSuit.HEARTS);
        } else {
            card5 = new Card(0, cardRank, CardSuit.HEARTS);
        }

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createFullHouse(CardRank primaryCardRank, CardRank secondaryCardRank) {
        Card card1 = new Card(0, primaryCardRank, CardSuit.HEARTS);
        Card card2 = new Card(0, secondaryCardRank, CardSuit.CLUBS);
        Card card3 = new Card(0, secondaryCardRank, CardSuit.DIAMONDS);
        Card card4 = new Card(0, primaryCardRank, CardSuit.SPADES);
        Card card5 = new Card(0, primaryCardRank, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

}
