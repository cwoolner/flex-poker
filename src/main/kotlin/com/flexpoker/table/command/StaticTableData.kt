package com.flexpoker.table.command

enum class HandRanking {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH
}

enum class PlayerAction {
    CHECK, FOLD, CALL, RAISE
}

enum class HandDealerState {
    NONE, POCKET_CARDS_DEALT, FLOP_DEALT, TURN_DEALT, RIVER_DEALT, COMPLETE
}

data class Card (val id: Int, val cardRank: CardRank, val cardSuit: CardSuit) : Comparable<Card> {
    override fun compareTo(other: Card): Int {
        return cardRank.compareTo(other.cardRank)
    }
}

enum class CardRank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

enum class CardSuit {
    HEARTS, SPADES, DIAMONDS, CLUBS
}

data class CardsUsedInHand(val flopCards: FlopCards, val turnCard: TurnCard,
                           val riverCard: RiverCard, val pocketCards: List<PocketCards>)

data class FlopCards (val card1: Card, val card2: Card, val card3: Card)

data class PocketCards (val card1: Card, val card2: Card)

data class RiverCard (val card: Card)

data class TurnCard (val card: Card)