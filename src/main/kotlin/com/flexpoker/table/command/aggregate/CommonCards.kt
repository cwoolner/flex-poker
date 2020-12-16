package com.flexpoker.table.command.aggregate

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard

class CommonCards(flopCards: FlopCards, turnCard: TurnCard, riverCard: RiverCard) {
    val cards: List<Card> = listOf(flopCards.card1, flopCards.card2, flopCards.card3, turnCard.card, riverCard.card)
    val flopCards: FlopCards
        get() = FlopCards(cards[0], cards[1], cards[2])
    val turnCard: TurnCard
        get() = TurnCard(cards[3])
    val riverCard: RiverCard
        get() = RiverCard(cards[4])
}