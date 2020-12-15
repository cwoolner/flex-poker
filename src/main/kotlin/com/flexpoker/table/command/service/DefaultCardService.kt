package com.flexpoker.table.command.service

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.CardsUsedInHand
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import org.springframework.stereotype.Service

@Service
class DefaultCardService : CardService {

    override fun createShuffledDeck(): List<Card> {
        return deckOfCards.shuffled()
    }

    override fun createCardsUsedInHand(cards: List<Card>, numberOfPlayers: Int): CardsUsedInHand {
        val flopCardsIndex = numberOfPlayers * 2 + 1
        val flopCards = FlopCards(cards[flopCardsIndex], cards[flopCardsIndex + 1], cards[flopCardsIndex + 2])
        val turnCard = TurnCard(cards[flopCardsIndex + 4])
        val riverCard = RiverCard(cards[flopCardsIndex + 6])
        val pocketCards = (0 until numberOfPlayers).map { PocketCards(cards[it], cards[it + numberOfPlayers]) }
        return CardsUsedInHand(flopCards, turnCard, riverCard, pocketCards)
    }

    companion object {
        private val deckOfCards: List<Card> = listOf(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(1, CardRank.THREE, CardSuit.HEARTS),
            Card(2, CardRank.FOUR, CardSuit.HEARTS),
            Card(3, CardRank.FIVE, CardSuit.HEARTS),
            Card(4, CardRank.SIX, CardSuit.HEARTS),
            Card(5, CardRank.SEVEN, CardSuit.HEARTS),
            Card(6, CardRank.EIGHT, CardSuit.HEARTS),
            Card(7, CardRank.NINE, CardSuit.HEARTS),
            Card(8, CardRank.TEN, CardSuit.HEARTS),
            Card(9, CardRank.JACK, CardSuit.HEARTS),
            Card(10, CardRank.QUEEN, CardSuit.HEARTS),
            Card(11, CardRank.KING, CardSuit.HEARTS),
            Card(12, CardRank.ACE, CardSuit.HEARTS),
            Card(13, CardRank.TWO, CardSuit.SPADES),
            Card(14, CardRank.THREE, CardSuit.SPADES),
            Card(15, CardRank.FOUR, CardSuit.SPADES),
            Card(16, CardRank.FIVE, CardSuit.SPADES),
            Card(17, CardRank.SIX, CardSuit.SPADES),
            Card(18, CardRank.SEVEN, CardSuit.SPADES),
            Card(19, CardRank.EIGHT, CardSuit.SPADES),
            Card(20, CardRank.NINE, CardSuit.SPADES),
            Card(21, CardRank.TEN, CardSuit.SPADES),
            Card(22, CardRank.JACK, CardSuit.SPADES),
            Card(23, CardRank.QUEEN, CardSuit.SPADES),
            Card(24, CardRank.KING, CardSuit.SPADES),
            Card(25, CardRank.ACE, CardSuit.SPADES),
            Card(26, CardRank.TWO, CardSuit.DIAMONDS),
            Card(27, CardRank.THREE, CardSuit.DIAMONDS),
            Card(28, CardRank.FOUR, CardSuit.DIAMONDS),
            Card(29, CardRank.FIVE, CardSuit.DIAMONDS),
            Card(30, CardRank.SIX, CardSuit.DIAMONDS),
            Card(31, CardRank.SEVEN, CardSuit.DIAMONDS),
            Card(32, CardRank.EIGHT, CardSuit.DIAMONDS),
            Card(33, CardRank.NINE, CardSuit.DIAMONDS),
            Card(34, CardRank.TEN, CardSuit.DIAMONDS),
            Card(35, CardRank.JACK, CardSuit.DIAMONDS),
            Card(36, CardRank.QUEEN, CardSuit.DIAMONDS),
            Card(37, CardRank.KING, CardSuit.DIAMONDS),
            Card(38, CardRank.ACE, CardSuit.DIAMONDS),
            Card(39, CardRank.TWO, CardSuit.CLUBS),
            Card(40, CardRank.THREE, CardSuit.CLUBS),
            Card(41, CardRank.FOUR, CardSuit.CLUBS),
            Card(42, CardRank.FIVE, CardSuit.CLUBS),
            Card(43, CardRank.SIX, CardSuit.CLUBS),
            Card(44, CardRank.SEVEN, CardSuit.CLUBS),
            Card(45, CardRank.EIGHT, CardSuit.CLUBS),
            Card(46, CardRank.NINE, CardSuit.CLUBS),
            Card(47, CardRank.TEN, CardSuit.CLUBS),
            Card(48, CardRank.JACK, CardSuit.CLUBS),
            Card(49, CardRank.QUEEN, CardSuit.CLUBS),
            Card(50, CardRank.KING, CardSuit.CLUBS),
            Card(51, CardRank.ACE, CardSuit.CLUBS)
        )
    }

}