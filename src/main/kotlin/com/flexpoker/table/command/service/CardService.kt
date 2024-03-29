package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardsUsedInHand

interface CardService {
    fun createShuffledDeck(): List<Card>
    fun createCardsUsedInHand(cards: List<Card>, numberOfPlayers: Int): CardsUsedInHand
}