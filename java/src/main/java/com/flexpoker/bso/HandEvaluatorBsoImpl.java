package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flexpoker.model.Card;
import com.flexpoker.model.CardSuit;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.PocketCards;

@Service("handEvaluatorBso")
public class HandEvaluatorBsoImpl implements HandEvaluatorBso {

    @Override
    public List<HandRanking> determinePossibleHands(CommonCards commonCards) {
        List<HandRanking> possibleHandRankings = new ArrayList<HandRanking>();

        CommonCardStatus straightFlushStatus =
                determineStraightFlushStatus(commonCards);

        if (straightFlushStatus == CommonCardStatus.BOARD) {
            possibleHandRankings.add(HandRanking.STRAIGHT_FLUSH);
            return possibleHandRankings;
        }

        if (straightFlushStatus == CommonCardStatus.POSSIBLE) {
            possibleHandRankings.add(HandRanking.STRAIGHT_FLUSH);
            possibleHandRankings.add(HandRanking.FLUSH);
            possibleHandRankings.add(HandRanking.STRAIGHT);
        }

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

    private CommonCardStatus determineStraightFlushStatus(CommonCards commonCards) {
        boolean straight = isStraight(commonCards);
        boolean flush = isFlush(commonCards);

        if (straight && flush) {
            return CommonCardStatus.BOARD;
        }

        return null;
    }

    private boolean isFlush(CommonCards commonCards) {
        CardSuit cardSuit = null;

        for (Card card : commonCards.getCards()) {
            if (cardSuit == null) {
                cardSuit = card.getCardSuit();
                continue;
            }
            if (!cardSuit.equals(card.getCardSuit())) {
                return false;
            }
        }

        return true;
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

}
