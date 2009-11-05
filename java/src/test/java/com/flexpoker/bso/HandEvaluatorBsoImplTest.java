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

    private void verifyStraightOnBoardHelper(CardRank cardRank) {
        CommonCards commonCards = createStraight(cardRank);
        List<HandRanking> handRankings = bso.determinePossibleHands(commonCards);
        assertEquals(1, handRankings.size());
        assertEquals(HandRanking.STRAIGHT, handRankings.get(0));
    }

    private CommonCards createStraight(CardRank cardRank) {
        Card card1 = null;
        Card card2 = null;
        Card card3 = null;
        Card card4 = null;
        Card card5 = null;

        CardRank[] cardRanks = CardRank.values();

        // if 5, make the first one an ace for the wheel
        if (cardRank.ordinal() == 3) {
            card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        } else {
            card1 = new Card(0, cardRanks[cardRank.ordinal() - 4], CardSuit.HEARTS);
        }

        card2 = new Card(0, cardRanks[cardRank.ordinal() - 3], CardSuit.CLUBS);
        card3 = new Card(0, cardRanks[cardRank.ordinal() - 2], CardSuit.DIAMONDS);
        card4 = new Card(0, cardRanks[cardRank.ordinal() - 1], CardSuit.SPADES);
        card5 = new Card(0, cardRank, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card2, card4, card5);
        TurnCard turnCard = new TurnCard(card3);
        RiverCard riverCard = new RiverCard(card1);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

    private CommonCards createStraightFlush(CardRank cardRank) {
        Card card1 = null;
        Card card2 = null;
        Card card3 = null;
        Card card4 = null;
        Card card5 = null;

        CardRank[] cardRanks = CardRank.values();

        // if 5, make the first one an ace for the wheel
        if (cardRank.ordinal() == 3) {
            card1 = new Card(0, CardRank.ACE, CardSuit.HEARTS);
        } else {
            card1 = new Card(0, cardRanks[cardRank.ordinal() - 4], CardSuit.HEARTS);
        }

        card2 = new Card(0, cardRanks[cardRank.ordinal() - 3], CardSuit.HEARTS);
        card3 = new Card(0, cardRanks[cardRank.ordinal() - 2], CardSuit.HEARTS);
        card4 = new Card(0, cardRanks[cardRank.ordinal() - 1], CardSuit.HEARTS);
        card5 = new Card(0, cardRank, CardSuit.HEARTS);

        FlopCards flopCards = new FlopCards(card1, card3, card4);
        TurnCard turnCard = new TurnCard(card5);
        RiverCard riverCard = new RiverCard(card2);

        return new CommonCards(flopCards, turnCard, riverCard);
    }

}
