package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexpoker.model.Card;
import com.flexpoker.model.CardRank;
import com.flexpoker.model.CardSuit;
import com.flexpoker.model.CommonCards;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.User;

@Service
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
    public HandEvaluation determineHandEvaluation(CommonCards commonCards, User user,
            PocketCards pocketCards, List<HandRanking> possibleHandRankings) {
        HandEvaluation handEvaluation = new HandEvaluation();
        handEvaluation.setUser(user);

        List<Card> cardList = new ArrayList<Card>(commonCards.getCards());
        cardList.add(pocketCards.getCard1());
        cardList.add(pocketCards.getCard2());

        fillInHandEvaluation(handEvaluation, cardList, possibleHandRankings);

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

    private void fillInHandEvaluation(HandEvaluation handEvaluation,
            List<Card> cardList, List<HandRanking> possibleHandRankings) {
        Collections.sort(possibleHandRankings);
        Collections.reverse(possibleHandRankings);

        for (HandRanking handRanking : possibleHandRankings) {
            switch (handRanking) {
                case STRAIGHT_FLUSH:
                    if (evaluateStraightFlush(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.STRAIGHT_FLUSH);
                        return;
                    }
                case FOUR_OF_A_KIND:
                    if (evaluateFourOfAKind(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.FOUR_OF_A_KIND);
                        return;
                    }
                case FULL_HOUSE:
                    if (evaluateFullHouse(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.FULL_HOUSE);
                        return;
                    }
                case FLUSH:
                    if (evaluateFlush(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.FLUSH);
                        return;
                    }
                case STRAIGHT:
                    if (evaluateStraight(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.STRAIGHT);
                        return;
                    }
                case THREE_OF_A_KIND:
                    if (evaluateThreeOfAKind(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.THREE_OF_A_KIND);
                        return;
                    }
                case TWO_PAIR:
                    if (evaluateTwoPair(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.TWO_PAIR);
                        return;
                    }
                case ONE_PAIR:
                    if (evaluateOnePair(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.ONE_PAIR);
                        return;
                    }
                case HIGH_CARD:
                    if (evaluateHighCard(handEvaluation, cardList)) {
                        handEvaluation.setHandRanking(HandRanking.HIGH_CARD);
                        return;
                    }
            }
        }
    }

    private boolean evaluateStraightFlush(HandEvaluation handEvaluation, List<Card> cardList) {
        cardList = findCardsInLargestSuit(cardList);

        if (cardList.size() < 5) {
            return false;
        }

        CardRank cardRank = findCardRankOfHighestStraight(cardList);

        if (cardRank == null) {
            return false;
        }

        handEvaluation.setPrimaryCardRank(cardRank);
        return true;
    }

    private boolean evaluateFourOfAKind(HandEvaluation handEvaluation, List<Card> cardList) {
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();
        CardRank cardRank6 = cardList.get(5).getCardRank();
        CardRank cardRank7 = cardList.get(6).getCardRank();

        if (cardRank1 == cardRank4) {
            handEvaluation.setPrimaryCardRank(cardRank1);
            handEvaluation.setFirstKicker(cardRank7);
            return true;
        }
        if (cardRank2 == cardRank5) {
            handEvaluation.setPrimaryCardRank(cardRank2);
            handEvaluation.setFirstKicker(cardRank7);
            return true;
        }
        if (cardRank3 == cardRank6) {
            handEvaluation.setPrimaryCardRank(cardRank3);
            handEvaluation.setFirstKicker(cardRank7);
            return true;
        }
        if (cardRank4 == cardRank7) {
            handEvaluation.setPrimaryCardRank(cardRank4);
            handEvaluation.setFirstKicker(cardRank3);
            return true;
        }

        return false;
    }

    private boolean evaluateFullHouse(HandEvaluation handEvaluation, List<Card> cardList) {
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();
        CardRank cardRank6 = cardList.get(5).getCardRank();
        CardRank cardRank7 = cardList.get(6).getCardRank();

        if (cardRank5 == cardRank7) {
            if (cardRank3 == cardRank4) {
                handEvaluation.setPrimaryCardRank(cardRank5);
                handEvaluation.setSecondaryCardRank(cardRank3);
                return true;
            }
            if (cardRank2 == cardRank3) {
                handEvaluation.setPrimaryCardRank(cardRank5);
                handEvaluation.setSecondaryCardRank(cardRank2);
                return true;
            }
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank5);
                handEvaluation.setSecondaryCardRank(cardRank1);
                return true;
            }
        }
        if (cardRank4 == cardRank6) {
            if (cardRank2 == cardRank3) {
                handEvaluation.setPrimaryCardRank(cardRank4);
                handEvaluation.setSecondaryCardRank(cardRank2);
                return true;
            }
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank4);
                handEvaluation.setSecondaryCardRank(cardRank1);
                return true;
            }
        }
        if (cardRank3 == cardRank5) {
            if (cardRank6 == cardRank7) {
                handEvaluation.setPrimaryCardRank(cardRank3);
                handEvaluation.setSecondaryCardRank(cardRank6);
                return true;
            }
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank3);
                handEvaluation.setSecondaryCardRank(cardRank1);
                return true;
            }
        }
        if (cardRank2 == cardRank4) {
            if (cardRank6 == cardRank7) {
                handEvaluation.setPrimaryCardRank(cardRank2);
                handEvaluation.setSecondaryCardRank(cardRank6);
                return true;
            }
            if (cardRank5 == cardRank6) {
                handEvaluation.setPrimaryCardRank(cardRank2);
                handEvaluation.setSecondaryCardRank(cardRank5);
                return true;
            }
        }
        if (cardRank1 == cardRank3) {
            if (cardRank6 == cardRank7) {
                handEvaluation.setPrimaryCardRank(cardRank1);
                handEvaluation.setSecondaryCardRank(cardRank6);
                return true;
            }
            if (cardRank5 == cardRank6) {
                handEvaluation.setPrimaryCardRank(cardRank1);
                handEvaluation.setSecondaryCardRank(cardRank5);
                return true;
            }
            if (cardRank4 == cardRank5) {
                handEvaluation.setPrimaryCardRank(cardRank1);
                handEvaluation.setSecondaryCardRank(cardRank4);
                return true;
            }
        }

        return false;
    }

    private boolean evaluateFlush(HandEvaluation handEvaluation, List<Card> cardList) {
        List<Card> cards = findCardsInLargestSuit(cardList);

        if (cards.size() < 5) {
            return false;
        }

        Collections.sort(cards);

        handEvaluation.setPrimaryCardRank(cards.get(cards.size() - 1).getCardRank());
        handEvaluation.setFirstKicker(cards.get(cards.size() - 2).getCardRank());
        handEvaluation.setSecondKicker(cards.get(cards.size() - 3).getCardRank());
        handEvaluation.setThirdKicker(cards.get(cards.size() - 4).getCardRank());
        handEvaluation.setFourthKicker(cards.get(cards.size() - 5).getCardRank());
        return true;
    }

    private boolean evaluateStraight(HandEvaluation handEvaluation, List<Card> cardList) {
        CardRank cardRank = findCardRankOfHighestStraight(cardList);

        if (cardRank == null) {
            return false;
        }

        handEvaluation.setPrimaryCardRank(cardRank);
        return true;
    }

    private boolean evaluateThreeOfAKind(HandEvaluation handEvaluation, List<Card> cardList) {
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();
        CardRank cardRank6 = cardList.get(5).getCardRank();
        CardRank cardRank7 = cardList.get(6).getCardRank();

        if (cardRank5 == cardRank7) {
            handEvaluation.setPrimaryCardRank(cardRank5);
            handEvaluation.setFirstKicker(cardRank4);
            handEvaluation.setSecondKicker(cardRank3);
            return true;
        }
        if (cardRank4 == cardRank6) {
            handEvaluation.setPrimaryCardRank(cardRank4);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank3);
            return true;
        }
        if (cardRank3 == cardRank5) {
            handEvaluation.setPrimaryCardRank(cardRank3);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            return true;
        }
        if (cardRank2 == cardRank4) {
            handEvaluation.setPrimaryCardRank(cardRank2);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            return true;
        }
        if (cardRank1 == cardRank3) {
            handEvaluation.setPrimaryCardRank(cardRank1);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            return true;
        }

        return false;
    }

    private boolean evaluateTwoPair(HandEvaluation handEvaluation, List<Card> cardList) {
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();
        CardRank cardRank6 = cardList.get(5).getCardRank();
        CardRank cardRank7 = cardList.get(6).getCardRank();

        if (cardRank6 == cardRank7) {
            if (cardRank4 == cardRank5) {
                handEvaluation.setPrimaryCardRank(cardRank6);
                handEvaluation.setSecondaryCardRank(cardRank4);
                handEvaluation.setFirstKicker(cardRank3);
                return true;
            }
            if (cardRank3 == cardRank4) {
                handEvaluation.setPrimaryCardRank(cardRank6);
                handEvaluation.setSecondaryCardRank(cardRank3);
                handEvaluation.setFirstKicker(cardRank5);
                return true;
            }
            if (cardRank2 == cardRank3) {
                handEvaluation.setPrimaryCardRank(cardRank6);
                handEvaluation.setSecondaryCardRank(cardRank2);
                handEvaluation.setFirstKicker(cardRank5);
                return true;
            }
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank6);
                handEvaluation.setSecondaryCardRank(cardRank1);
                handEvaluation.setFirstKicker(cardRank5);
                return true;
            }
        }
        if (cardRank5 == cardRank6) {
            if (cardRank3 == cardRank4) {
                handEvaluation.setPrimaryCardRank(cardRank5);
                handEvaluation.setSecondaryCardRank(cardRank3);
                handEvaluation.setFirstKicker(cardRank7);
                return true;
            }
            if (cardRank2 == cardRank3) {
                handEvaluation.setPrimaryCardRank(cardRank5);
                handEvaluation.setSecondaryCardRank(cardRank2);
                handEvaluation.setFirstKicker(cardRank7);
                return true;
            }
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank5);
                handEvaluation.setSecondaryCardRank(cardRank1);
                handEvaluation.setFirstKicker(cardRank7);
                return true;
            }
        }
        if (cardRank4 == cardRank5) {
            if (cardRank2 == cardRank3) {
                handEvaluation.setPrimaryCardRank(cardRank4);
                handEvaluation.setSecondaryCardRank(cardRank2);
                handEvaluation.setFirstKicker(cardRank7);
                return true;
            }
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank4);
                handEvaluation.setSecondaryCardRank(cardRank1);
                handEvaluation.setFirstKicker(cardRank7);
                return true;
            }
        }
        if (cardRank3 == cardRank4) {
            if (cardRank1 == cardRank2) {
                handEvaluation.setPrimaryCardRank(cardRank4);
                handEvaluation.setSecondaryCardRank(cardRank1);
                handEvaluation.setFirstKicker(cardRank7);
                return true;
            }
        }

        return false;
    }

    private boolean evaluateOnePair(HandEvaluation handEvaluation, List<Card> cardList) {
        Collections.sort(cardList);

        CardRank cardRank1 = cardList.get(0).getCardRank();
        CardRank cardRank2 = cardList.get(1).getCardRank();
        CardRank cardRank3 = cardList.get(2).getCardRank();
        CardRank cardRank4 = cardList.get(3).getCardRank();
        CardRank cardRank5 = cardList.get(4).getCardRank();
        CardRank cardRank6 = cardList.get(5).getCardRank();
        CardRank cardRank7 = cardList.get(6).getCardRank();

        if (cardRank6 == cardRank7) {
            handEvaluation.setPrimaryCardRank(cardRank6);
            handEvaluation.setFirstKicker(cardRank5);
            handEvaluation.setSecondKicker(cardRank4);
            handEvaluation.setThirdKicker(cardRank3);
            return true;
        }
        if (cardRank5 == cardRank6) {
            handEvaluation.setPrimaryCardRank(cardRank5);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank4);
            handEvaluation.setThirdKicker(cardRank3);
            return true;
        }
        if (cardRank4 == cardRank5) {
            handEvaluation.setPrimaryCardRank(cardRank4);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            handEvaluation.setThirdKicker(cardRank3);
            return true;
        }
        if (cardRank3 == cardRank4) {
            handEvaluation.setPrimaryCardRank(cardRank3);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            handEvaluation.setThirdKicker(cardRank5);
            return true;
        }
        if (cardRank2 == cardRank3) {
            handEvaluation.setPrimaryCardRank(cardRank2);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            handEvaluation.setThirdKicker(cardRank5);
            return true;
        }
        if (cardRank1 == cardRank2) {
            handEvaluation.setPrimaryCardRank(cardRank1);
            handEvaluation.setFirstKicker(cardRank7);
            handEvaluation.setSecondKicker(cardRank6);
            handEvaluation.setThirdKicker(cardRank5);
            return true;
        }

        return false;
    }

    private boolean evaluateHighCard(HandEvaluation handEvaluation, List<Card> cardList) {
        Collections.sort(cardList);

        handEvaluation.setPrimaryCardRank(cardList.get(6).getCardRank());
        handEvaluation.setFirstKicker(cardList.get(5).getCardRank());
        handEvaluation.setSecondKicker(cardList.get(4).getCardRank());
        handEvaluation.setThirdKicker(cardList.get(3).getCardRank());
        handEvaluation.setFourthKicker(cardList.get(2).getCardRank());

        return true;
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
        List<Card> cards = findCardsInLargestSuit(commonCards.getCards());

        // we have a flush, check to see if it's a straight also
        if (cards.size() == 5 && isStraight(commonCards)) {
            return CommonCardStatus.BOARD;
        }

        if (cards.size() < 3) {
            return CommonCardStatus.NOT_POSSIBLE;
        }

        if (isStraightPossible(cards)) {
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
        List<Card> cards = findCardsInLargestSuit(commonCards.getCards());

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

        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.ACE,
                CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.TWO,
                CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.THREE,
                CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.FOUR,
                CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.FIVE,
                CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.SIX,
                CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.SEVEN,
                CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.EIGHT,
                CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.NINE,
                CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING}), 3)) {
            return true;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.TEN,
                CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE}), 3)) {
            return true;
        }

        return false;
    }

    private boolean doCardRanksMatch(List<CardRank> cardRanks,
            List<CardRank> straightCardRanks, int numberToMatch) {

        Set<CardRank> cardRankSet = new HashSet<CardRank>(cardRanks);

        int numberOfMatched = 0;

        for (CardRank cardRank : cardRankSet) {
            if (straightCardRanks.contains(cardRank)) {
                numberOfMatched++;
            }
        }

        return numberOfMatched >= numberToMatch;
    }

    /**
     * Return the list of cards in the largest suit.  If that number is three,
     * then the correct suit will be represented.  If that number is only two,
     * then the first suit scanned will be returned in the following order:
     * HEARTS, CLUBS, DIAMONDS, SPADES
     */
    private List<Card> findCardsInLargestSuit(List<Card> cards) {
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

    private CardRank findCardRankOfHighestStraight(List<Card> cardList) {
        if (cardList.size() < 5) {
            return null;
        }

        List<CardRank> cardRanks = new ArrayList<CardRank>();

        for (Card card : cardList) {
            cardRanks.add(card.getCardRank());
        }

        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.TEN,
                CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE}), 5)) {
            return CardRank.ACE;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.NINE,
                CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING}), 5)) {
            return CardRank.KING;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.EIGHT,
                CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN}), 5)) {
            return CardRank.QUEEN;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.SEVEN,
                CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK}), 5)) {
            return CardRank.JACK;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.SIX,
                CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN}), 5)) {
            return CardRank.TEN;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.FIVE,
                CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE}), 5)) {
            return CardRank.NINE;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.FOUR,
                CardRank.FIVE, CardRank.SIX, CardRank.SEVEN, CardRank.EIGHT}), 5)) {
            return CardRank.EIGHT;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.THREE,
                CardRank.FOUR, CardRank.FIVE, CardRank.SIX, CardRank.SEVEN}), 5)) {
            return CardRank.SEVEN;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.TWO,
                CardRank.THREE, CardRank.FOUR, CardRank.FIVE, CardRank.SIX}), 5)) {
            return CardRank.SIX;
        }
        if (doCardRanksMatch(cardRanks, Arrays.asList(new CardRank[]{CardRank.ACE,
                CardRank.TWO, CardRank.THREE, CardRank.FOUR, CardRank.FIVE}), 5)) {
            return CardRank.FIVE;
        }

        return null;
    }

}
