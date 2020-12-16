package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.HandRanking
import java.util.UUID

class HandEvaluation : Comparable<HandEvaluation> {
    var playerId: UUID? = null
    var handRanking: HandRanking? = null
    var primaryCardRank: CardRank? = null
    var secondaryCardRank: CardRank? = null
    var firstKicker: CardRank? = null
    var secondKicker: CardRank? = null
    var thirdKicker: CardRank? = null
    var fourthKicker: CardRank? = null

    override fun compareTo(other: HandEvaluation): Int {
        return if (handRanking !== other.handRanking) {
            handRanking!!.compareTo(other.handRanking!!)
        } else when (handRanking) {
            HandRanking.HIGH_CARD -> highCardCompareTo(other)
            HandRanking.ONE_PAIR -> onePairCompareTo(other)
            HandRanking.TWO_PAIR -> twoPairCompareTo(other)
            HandRanking.THREE_OF_A_KIND -> threeOfAKindCompareTo(other)
            HandRanking.STRAIGHT -> straightCompareTo(other)
            HandRanking.FLUSH -> flushCompareTo(other)
            HandRanking.FULL_HOUSE -> fullHouseCompareTo(other)
            HandRanking.FOUR_OF_A_KIND -> fourOfAKindCompareTo(other)
            HandRanking.STRAIGHT_FLUSH -> straightFlushCompareTo(other)
            else -> throw IllegalArgumentException("The given HandEvaluation could not be correctly compared.")
        }
    }

    private fun highCardCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            if (firstKicker == otherHandEvaluation.firstKicker) {
                if (secondKicker == otherHandEvaluation.secondKicker) {
                    if (thirdKicker == otherHandEvaluation.thirdKicker) {
                        fourthKicker!!.compareTo(otherHandEvaluation.fourthKicker!!)
                    } else thirdKicker!!.compareTo(otherHandEvaluation.thirdKicker!!)
                } else secondKicker!!.compareTo(otherHandEvaluation.secondKicker!!)
            } else firstKicker!!.compareTo(otherHandEvaluation.firstKicker!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun onePairCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            if (firstKicker == otherHandEvaluation.firstKicker) {
                if (secondKicker == otherHandEvaluation.secondKicker) {
                    thirdKicker!!.compareTo(otherHandEvaluation.thirdKicker!!)
                } else secondKicker!!.compareTo(otherHandEvaluation.secondKicker!!)
            } else firstKicker!!.compareTo(otherHandEvaluation.firstKicker!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun twoPairCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            if (secondaryCardRank == otherHandEvaluation.secondaryCardRank) {
                firstKicker!!.compareTo(otherHandEvaluation.firstKicker!!)
            } else secondaryCardRank!!.compareTo(otherHandEvaluation.secondaryCardRank!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun threeOfAKindCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            if (firstKicker == otherHandEvaluation.firstKicker) {
                secondKicker!!.compareTo(otherHandEvaluation.secondKicker!!)
            } else firstKicker!!.compareTo(otherHandEvaluation.firstKicker!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun straightCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun flushCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            if (firstKicker == otherHandEvaluation.firstKicker) {
                if (secondKicker == otherHandEvaluation.secondKicker) {
                    if (thirdKicker == otherHandEvaluation.thirdKicker) {
                        fourthKicker!!.compareTo(otherHandEvaluation.fourthKicker!!)
                    } else thirdKicker!!.compareTo(otherHandEvaluation.thirdKicker!!)
                } else secondKicker!!.compareTo(otherHandEvaluation.secondKicker!!)
            } else firstKicker!!.compareTo(otherHandEvaluation.firstKicker!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun fullHouseCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            secondaryCardRank!!.compareTo(otherHandEvaluation.secondaryCardRank!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun fourOfAKindCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return if (primaryCardRank == otherHandEvaluation.primaryCardRank) {
            firstKicker!!.compareTo(otherHandEvaluation.firstKicker!!)
        } else primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }

    private fun straightFlushCompareTo(otherHandEvaluation: HandEvaluation): Int {
        return primaryCardRank!!.compareTo(otherHandEvaluation.primaryCardRank!!)
    }
}