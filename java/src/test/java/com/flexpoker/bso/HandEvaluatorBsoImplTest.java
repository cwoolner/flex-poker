package com.flexpoker.bso;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.flexpoker.model.Card;
import com.flexpoker.model.CardRank;
import com.flexpoker.model.CardSuit;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.TurnCard;
import com.flexpoker.util.Context;


public class HandEvaluatorBsoImplTest {

    private HandEvaluatorBsoImpl bso = (HandEvaluatorBsoImpl) Context.instance()
            .getBean("handEvaluatorBso");

    @Test
    public void testDeterminePossibleHands() {
        verifyStraightFlushOnBoard();
        verifyFourOfAKindOnBoard();
        verifyFullHouseOnBoard();
        verifyFlushOnBoard();
        verifyStraightOnBoard();
    }

    @Test
    public void testDetermineHandEvaluation() {
        fail("Not yet implemented");
    }

    private void verifyStraightFlushOnBoard() {
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

    private void verifyFourOfAKindOnBoard() {
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

    private void verifyFullHouseOnBoard() {
        verifyFullHouseOnBoardHelper(CardRank.TWO, CardRank.ACE);
        verifyFullHouseOnBoardHelper(CardRank.ACE, CardRank.TWO);
        verifyFullHouseOnBoardHelper(CardRank.KING, CardRank.ACE);
        verifyFullHouseOnBoardHelper(CardRank.ACE, CardRank.KING);
        verifyFullHouseOnBoardHelper(CardRank.KING, CardRank.QUEEN);
        verifyFullHouseOnBoardHelper(CardRank.EIGHT, CardRank.SEVEN);
    }

    private void verifyFlushOnBoard() {
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

    private void verifyStraightOnBoard() {
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
