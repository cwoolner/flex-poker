package com.flexpoker.test.util.datageneration

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard

object CardGenerator {

    private val CARD_1 = Card(1, CardRank.ACE, CardSuit.CLUBS)
    private val CARD_2 = Card(2, CardRank.TWO, CardSuit.CLUBS)
    private val CARD_3 = Card(3, CardRank.THREE, CardSuit.CLUBS)
    private val CARD_4 = Card(4, CardRank.FOUR, CardSuit.CLUBS)
    private val CARD_5 = Card(5, CardRank.FIVE, CardSuit.CLUBS)
    private val CARD_6 = Card(6, CardRank.SIX, CardSuit.CLUBS)
    private val CARD_7 = Card(7, CardRank.SEVEN, CardSuit.CLUBS)
    private val CARD_8 = Card(8, CardRank.EIGHT, CardSuit.CLUBS)
    private val CARD_9 = Card(9, CardRank.NINE, CardSuit.CLUBS)
    private val pocketCards1 = PocketCards(CARD_6, CARD_7)
    private val pocketCards2 = PocketCards(CARD_8, CARD_9)

    fun createFlopCards(): FlopCards {
        return FlopCards(CARD_1, CARD_2, CARD_3)
    }

    fun createTurnCard(): TurnCard {
        return TurnCard(CARD_4)
    }

    fun createRiverCard(): RiverCard {
        return RiverCard(CARD_5)
    }

    fun createPocketCards1(): PocketCards {
        return pocketCards1
    }

    fun createPocketCards2(): PocketCards {
        return pocketCards2
    }

}