package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flexpoker.model.Card;
import com.flexpoker.model.CardRank;
import com.flexpoker.model.CardSuit;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.PocketCards;

@Service("handEvaluatorBso")
public class HandEvaluatorBsoImpl implements HandEvaluatorBso {

    @Override
    public List<HandRanking> determinePossibleHands(CommonCards commonCards) {
        List<HandRanking> possibleHandRankings
                = new ArrayList<HandRanking>(Arrays.asList(HandRanking.values()));

        filterByStraightFlushStatus(commonCards, possibleHandRankings);
        filterByFourOfAKindStatus(commonCards, possibleHandRankings);
        filterByFullHouseStatus(commonCards, possibleHandRankings);
        filterByFlushStatus(commonCards, possibleHandRankings);
        filterByStraightStatus(commonCards, possibleHandRankings);
        filterByThreeOfAKindStatus(commonCards, possibleHandRankings);
        filterByTwoPairStatus(commonCards, possibleHandRankings);
        filterByOnePairStatus(commonCards, possibleHandRankings);

        return possibleHandRankings;
    }

    @Override
    public HandEvaluation determineHandEvaluation(CommonCards commonCards,
            PocketCards pocketCards, List<HandRanking> possibleHandRankings) {
        HandEvaluation handEvaluation = new HandEvaluation();
        return handEvaluation;
    }

    /**
     * Enum meant to be used as a return value for the hand ranking methods.
     * The returned value is then used to decide what hand rankings are
     * available.
     */
    private enum CommonCardStatus {
        NOT_POSSIBLE, POSSIBLE, BOARD;
    }

    private void filterByStraightFlushStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        CommonCardStatus straightFlushStatus = determineStraightFlushStatus(commonCards);

        if (straightFlushStatus == CommonCardStatus.BOARD) {
            possibleHandRankings.remove(HandRanking.FOUR_OF_A_KIND);
            possibleHandRankings.remove(HandRanking.FULL_HOUSE);
            possibleHandRankings.remove(HandRanking.FLUSH);
            possibleHandRankings.remove(HandRanking.STRAIGHT);
            possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND);
            possibleHandRankings.remove(HandRanking.TWO_PAIR);
            possibleHandRankings.remove(HandRanking.ONE_PAIR);
            possibleHandRankings.remove(HandRanking.HIGH_CARD);
        } else if (straightFlushStatus == CommonCardStatus.NOT_POSSIBLE) {
            possibleHandRankings.remove(HandRanking.STRAIGHT_FLUSH);
        }
    }

    private void filterByFourOfAKindStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.FOUR_OF_A_KIND)) {
            CommonCardStatus fourOfAKindStatus = determineFourOfAKindStatus(commonCards);

            if (fourOfAKindStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.STRAIGHT_FLUSH);
                possibleHandRankings.remove(HandRanking.FULL_HOUSE);
                possibleHandRankings.remove(HandRanking.FLUSH);
                possibleHandRankings.remove(HandRanking.STRAIGHT);
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND);
                possibleHandRankings.remove(HandRanking.TWO_PAIR);
                possibleHandRankings.remove(HandRanking.ONE_PAIR);
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            } else if (fourOfAKindStatus == CommonCardStatus.NOT_POSSIBLE) {
                possibleHandRankings.remove(HandRanking.FOUR_OF_A_KIND);
                possibleHandRankings.remove(HandRanking.FULL_HOUSE);
            }
        }
    }

    private void filterByFullHouseStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.FULL_HOUSE)) {
            CommonCardStatus fullHouseStatus = determineFullHouseStatus(commonCards);

            if (fullHouseStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.FLUSH);
                possibleHandRankings.remove(HandRanking.STRAIGHT);
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND);
                possibleHandRankings.remove(HandRanking.TWO_PAIR);
                possibleHandRankings.remove(HandRanking.ONE_PAIR);
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            }
        }
    }

    private void filterByFlushStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.FLUSH)) {
            CommonCardStatus flushStatus = determineFlushStatus(commonCards);

            if (flushStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.STRAIGHT);
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND);
                possibleHandRankings.remove(HandRanking.TWO_PAIR);
                possibleHandRankings.remove(HandRanking.ONE_PAIR);
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            } else if (flushStatus == CommonCardStatus.NOT_POSSIBLE) {
                possibleHandRankings.remove(HandRanking.FLUSH);
            }
        }
    }

    private void filterByStraightStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.STRAIGHT)) {
            CommonCardStatus straightStatus = determineStraightStatus(commonCards);

            if (straightStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND);
                possibleHandRankings.remove(HandRanking.TWO_PAIR);
                possibleHandRankings.remove(HandRanking.ONE_PAIR);
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            } else if (straightStatus == CommonCardStatus.NOT_POSSIBLE) {
                possibleHandRankings.remove(HandRanking.STRAIGHT);
            }
        }
    }

    private void filterByThreeOfAKindStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.THREE_OF_A_KIND)) {
            CommonCardStatus threeOfAKindStatus = determineThreeOfAKindStatus(commonCards);

            if (threeOfAKindStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.TWO_PAIR);
                possibleHandRankings.remove(HandRanking.ONE_PAIR);
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            }
        }
    }

    private void filterByTwoPairStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.TWO_PAIR)) {
            CommonCardStatus twoPairStatus = determineTwoPairStatus(commonCards);

            if (twoPairStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.THREE_OF_A_KIND);
                possibleHandRankings.remove(HandRanking.ONE_PAIR);
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            }
        }
    }

    private void filterByOnePairStatus(CommonCards commonCards, List<HandRanking> possibleHandRankings) {
        if (possibleHandRankings.contains(HandRanking.ONE_PAIR)) {
            CommonCardStatus onePairStatus = determineOnePairStatus(commonCards);

            if (onePairStatus == CommonCardStatus.BOARD) {
                possibleHandRankings.remove(HandRanking.HIGH_CARD);
            }
        }
    }

    private CommonCardStatus determineStraightFlushStatus(CommonCards commonCards) {
        List<Card> listOfCardsInLargestSuit = findCardsInLargestSuit(commonCards);

        // we have a flush, check to see if it's a straight also
        if (listOfCardsInLargestSuit.size() == 5 && isStraight(commonCards)) {
            return CommonCardStatus.BOARD;
        }

        if (listOfCardsInLargestSuit.size() < 3) {
            return CommonCardStatus.NOT_POSSIBLE;
        }

        if (isStraightPossible(listOfCardsInLargestSuit)) {
            return CommonCardStatus.POSSIBLE;
        }

        return CommonCardStatus.NOT_POSSIBLE;
    }

    private CommonCardStatus determineFourOfAKindStatus(CommonCards commonCards) {
        List<Card> cardList = commonCards.getCards();
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();

        if ((cardRank1 == cardRank2 && cardRank2 == cardRank3 && cardRank3 == cardRank4)
                || (cardRank2 == cardRank3 && cardRank3 == cardRank4 && cardRank4 == cardRank5)) {
            return CommonCardStatus.BOARD;
        }

        if (cardRank1 == cardRank2 || cardRank2 == cardRank3
                || cardRank3 == cardRank4 || cardRank4 == cardRank5) {
            return CommonCardStatus.POSSIBLE;
        }

        return CommonCardStatus.NOT_POSSIBLE;
    }

    private CommonCardStatus determineFullHouseStatus(CommonCards commonCards) {
        List<Card> cardList = commonCards.getCards();
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();

        if ((cardRank1 == cardRank2 && cardRank2 == cardRank3 && cardRank4 == cardRank5)
                || (cardRank1 == cardRank2 && cardRank3 == cardRank4 && cardRank4 == cardRank5)) {
            return CommonCardStatus.BOARD;
        }

        if (cardRank1 == cardRank2 || cardRank2 == cardRank3
                || cardRank3 == cardRank4 || cardRank4 == cardRank5) {
            return CommonCardStatus.POSSIBLE;
        }

        return CommonCardStatus.NOT_POSSIBLE;
    }

    private CommonCardStatus determineFlushStatus(CommonCards commonCards) {
        List<Card> cards = findCardsInLargestSuit(commonCards);

        switch (cards.size()) {
            case 5:
                return CommonCardStatus.BOARD;
            case 4:
                return CommonCardStatus.POSSIBLE;
            case 3:
                return CommonCardStatus.POSSIBLE;
            default:
                return CommonCardStatus.NOT_POSSIBLE;
        }
    }

    private CommonCardStatus determineStraightStatus(CommonCards commonCards) {
        if (isStraight(commonCards)) {
            return CommonCardStatus.BOARD;
        }

        if (isStraightPossible(commonCards.getCards())) {
            return CommonCardStatus.POSSIBLE;
        }

        return CommonCardStatus.NOT_POSSIBLE;
    }

    private CommonCardStatus determineThreeOfAKindStatus(CommonCards commonCards) {
        List<Card> cardList = commonCards.getCards();
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();

        if ((cardRank1 == cardRank2 && cardRank2 == cardRank3)
                || (cardRank2 == cardRank3 && cardRank3 == cardRank4)
                || (cardRank3 == cardRank4 && cardRank4 == cardRank5)) {
            return CommonCardStatus.BOARD;
        }

        return CommonCardStatus.POSSIBLE;
    }

    private CommonCardStatus determineTwoPairStatus(CommonCards commonCards) {
        List<Card> cardList = commonCards.getCards();
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();

        if ((cardRank1 == cardRank2 && cardRank3 == cardRank4)
                || (cardRank1 == cardRank2 && cardRank4 == cardRank5)
                || (cardRank2 == cardRank3 && cardRank4 == cardRank5)) {
            return CommonCardStatus.BOARD;
        }

        return CommonCardStatus.POSSIBLE;
    }

    private CommonCardStatus determineOnePairStatus(CommonCards commonCards) {
        List<Card> cardList = commonCards.getCards();
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();

        if (cardRank1 == cardRank2 || cardRank2 == cardRank3
                || cardRank3 == cardRank4|| cardRank4 == cardRank5) {
            return CommonCardStatus.BOARD;
        }

        return CommonCardStatus.POSSIBLE;
    }

    private boolean isStraight(CommonCards commonCards) {
        List<Card> cardList = commonCards.getCards();
        Collections.sort(cardList);

        int cardRankOrdinal1 = cardList.get(0).getCardRank().ordinal();
        int cardRankOrdinal2 = cardList.get(1).getCardRank().ordinal();
        int cardRankOrdinal3 = cardList.get(2).getCardRank().ordinal();
        int cardRankOrdinal4 = cardList.get(3).getCardRank().ordinal();
        int cardRankOrdinal5 = cardList.get(4).getCardRank().ordinal();

        if (cardRankOrdinal1 + 1 != cardRankOrdinal2
            || cardRankOrdinal2 + 1 != cardRankOrdinal3
            || cardRankOrdinal3 + 1 != cardRankOrdinal4) {
            return false;
        }

        // check to see if it's a 2, then see if the 5th card is an ace
        if (cardRankOrdinal1 == 0 && cardRankOrdinal5 == 12) {
            return true;
        }

        return cardRankOrdinal4 + 1 == cardRankOrdinal5;
    }

    private boolean isStraightPossible(List<Card> cardList) {
        List<CardRank> cardRanks = new ArrayList<CardRank>();

        for (Card card : cardList) {
            cardRanks.add(card.getCardRank());
        }

        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.ACE,
                CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.TWO,
                CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.THREE,
                CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.FOUR,
                CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.FIVE,
                CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.SIX,
                CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.SEVEN,
                CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.EIGHT,
                CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.NINE,
                CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING}))) {
            return true;
        }
        if (doThreeCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.TEN,
                CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE}))) {
            return true;
        }

        return false;
    }

    private boolean doThreeCardRanksMatch(List<CardRank> cardRanks,
            List<CardRank> straightCardRanks) {
        int numberOfMatched = 0;

        for (CardRank cardRank : cardRanks) {
            if (straightCardRanks.contains(cardRank)) {
                numberOfMatched++;
            }
        }

        return numberOfMatched >= 3;
    }

    /**
     * Return the list of cards in the largest suit.  If that number is three,
     * then the correct suit will be represented.  If that number is only two,
     * then the first suit scanned will be returned in the following order:
     * HEARTS, CLUBS, DIAMONDS, SPADES
     */
    private List<Card> findCardsInLargestSuit(CommonCards commonCards) {
        List<Card> cards = commonCards.getCards();
        Map<CardSuit, List<Card>> suitMap = new HashMap<CardSuit, List<Card>>();

        for (Card card : cards) {
            CardSuit cardSuit = card.getCardSuit();
            if (suitMap.get(cardSuit) == null) {
                suitMap.put(cardSuit, new ArrayList<Card>());
            }
            suitMap.get(cardSuit).add(card);
        }

        int numberOfHearts = suitMap.get(CardSuit.HEARTS) == null
                ? 0 : suitMap.get(CardSuit.HEARTS).size();
        int numberOfClubs = suitMap.get(CardSuit.CLUBS) == null
                ? 0 : suitMap.get(CardSuit.CLUBS).size();
        int numberOfDiamonds = suitMap.get(CardSuit.DIAMONDS) == null
                ? 0 : suitMap.get(CardSuit.DIAMONDS).size();
        int numberOfSpades = suitMap.get(CardSuit.SPADES) == null
                ? 0 : suitMap.get(CardSuit.SPADES).size();

        if (numberOfHearts >= 3) {
            return suitMap.get(CardSuit.HEARTS);
        }
        if (numberOfClubs >= 3) {
            return suitMap.get(CardSuit.CLUBS);
        }
        if (numberOfDiamonds >= 3) {
            return suitMap.get(CardSuit.DIAMONDS);
        }
        if (numberOfSpades >= 3) {
            return suitMap.get(CardSuit.SPADES);
        }

        if (numberOfHearts == 2) {
            return suitMap.get(CardSuit.HEARTS);
        }
        if (numberOfClubs == 2) {
            return suitMap.get(CardSuit.CLUBS);
        }
        if (numberOfDiamonds == 2) {
            return suitMap.get(CardSuit.DIAMONDS);
        }
        if (numberOfSpades == 2) {
            return suitMap.get(CardSuit.SPADES);
        }

        throw new IllegalArgumentException("CommonCards must contain at least "
                + "two instances of a suit.");
    }

}
