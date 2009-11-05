package com.flexpoker.model;

public class HandEvaluation implements Comparable<HandEvaluation> {

    private User user;

    private HandRanking handRanking;

    private CardRank primaryCardRanking;

    private CardRank secondaryCardRank;

    private CardRank firstKicker;

    private CardRank secondKicker;

    private CardRank thirdKicker;

    private CardRank fourthKicker;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HandRanking getHandRanking() {
        return handRanking;
    }

    public void setHandRanking(HandRanking handRanking) {
        this.handRanking = handRanking;
    }

    public CardRank getPrimaryCardRanking() {
        return primaryCardRanking;
    }

    public void setPrimaryCardRanking(CardRank primaryCardRanking) {
        this.primaryCardRanking = primaryCardRanking;
    }

    public CardRank getSecondaryCardRank() {
        return secondaryCardRank;
    }

    public void setSecondaryCardRank(CardRank secondaryCardRank) {
        this.secondaryCardRank = secondaryCardRank;
    }

    public CardRank getFirstKicker() {
        return firstKicker;
    }

    public void setFirstKicker(CardRank firstKicker) {
        this.firstKicker = firstKicker;
    }

    public CardRank getSecondKicker() {
        return secondKicker;
    }

    public void setSecondKicker(CardRank secondKicker) {
        this.secondKicker = secondKicker;
    }

    public CardRank getThirdKicker() {
        return thirdKicker;
    }

    public void setThirdKicker(CardRank thirdKicker) {
        this.thirdKicker = thirdKicker;
    }

    public CardRank getFourthKicker() {
        return fourthKicker;
    }

    public void setFourthKicker(CardRank fourthKicker) {
        this.fourthKicker = fourthKicker;
    }

    @Override
    public int compareTo(HandEvaluation otherHandEvaluation) {
        if (handRanking != otherHandEvaluation.handRanking) {
            return handRanking.compareTo(otherHandEvaluation.handRanking);
        }

        switch (handRanking) {
            case HIGH_CARD:
                return highCardCompareTo(otherHandEvaluation);
            case ONE_PAIR:
                return onePairCompareTo(otherHandEvaluation);
            case TWO_PAIR:
                return twoPairCompareTo(otherHandEvaluation);
            case THREE_OF_A_KIND:
                return threeOfAKindCompareTo(otherHandEvaluation);
            case STRAIGHT:
                return straightCompareTo(otherHandEvaluation);
            case FLUSH:
                return flushCompareTo(otherHandEvaluation);
            case FULL_HOUSE:
                return fullHouseCompareTo(otherHandEvaluation);
            case FOUR_OF_A_KIND:
                return fourOfAKindCompareTo(otherHandEvaluation);
            case STRAIGHT_FLUSH:
                return straightFlushCompareTo(otherHandEvaluation);
            default:
                throw new IllegalArgumentException("The given HandEvaluation "
                        + "could not be correctly compared.");
        }
    }

    private int highCardCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            if (firstKicker.equals(otherHandEvaluation.firstKicker)) {
                if (secondKicker.equals(otherHandEvaluation.secondKicker)) {
                    if (thirdKicker.equals(otherHandEvaluation.thirdKicker)) {
                        return fourthKicker.compareTo(otherHandEvaluation.fourthKicker);
                    }
                    return thirdKicker.compareTo(otherHandEvaluation.thirdKicker);
                }
                return secondKicker.compareTo(otherHandEvaluation.secondKicker);
            }
            return firstKicker.compareTo(otherHandEvaluation.firstKicker);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int onePairCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            if (firstKicker.equals(otherHandEvaluation.firstKicker)) {
                if (secondKicker.equals(otherHandEvaluation.secondKicker)) {
                    return thirdKicker.compareTo(otherHandEvaluation.thirdKicker);
                }
                return secondKicker.compareTo(otherHandEvaluation.secondKicker);
            }
            return firstKicker.compareTo(otherHandEvaluation.firstKicker);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int twoPairCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            if (secondaryCardRank.equals(otherHandEvaluation.secondaryCardRank)) {
                return firstKicker.compareTo(otherHandEvaluation.firstKicker);
            }
            return secondaryCardRank.compareTo(otherHandEvaluation.secondaryCardRank);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int threeOfAKindCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            if (firstKicker.equals(otherHandEvaluation.firstKicker)) {
                return secondKicker.compareTo(otherHandEvaluation.secondKicker);
            }
            return firstKicker.compareTo(otherHandEvaluation.firstKicker);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int straightCompareTo(HandEvaluation otherHandEvaluation) {
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int flushCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            if (firstKicker.equals(otherHandEvaluation.firstKicker)) {
                if (secondKicker.equals(otherHandEvaluation.secondKicker)) {
                    if (thirdKicker.equals(otherHandEvaluation.thirdKicker)) {
                        return fourthKicker.compareTo(otherHandEvaluation.fourthKicker);
                    }
                    return thirdKicker.compareTo(otherHandEvaluation.thirdKicker);
                }
                return secondKicker.compareTo(otherHandEvaluation.secondKicker);
            }
            return firstKicker.compareTo(otherHandEvaluation.firstKicker);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int fullHouseCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            return secondaryCardRank.compareTo(otherHandEvaluation.secondaryCardRank);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int fourOfAKindCompareTo(HandEvaluation otherHandEvaluation) {
        if (primaryCardRanking.equals(otherHandEvaluation.primaryCardRanking)) {
            return firstKicker.compareTo(otherHandEvaluation.firstKicker);
        }
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

    private int straightFlushCompareTo(HandEvaluation otherHandEvaluation) {
        return primaryCardRanking.compareTo(otherHandEvaluation.primaryCardRanking);
    }

}
