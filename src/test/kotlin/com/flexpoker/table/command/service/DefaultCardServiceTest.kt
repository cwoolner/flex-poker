package com.flexpoker.table.command.service

import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

@UnitTestClass
class DefaultCardServiceTest {

    @Test
    fun testCreateShuffledDeck() {
        val service = DefaultCardService()
        val shuffledDeck = service.createShuffledDeck()
        assertEquals(52, shuffledDeck.size)
    }

    @Test
    fun testShuffledDecksAreDifferent() {
        val service = DefaultCardService()
        val shuffledDeck1 = service.createShuffledDeck()
        val shuffledDeck2 = service.createShuffledDeck()
        assertFalse(shuffledDeck1 === shuffledDeck2)
        assertFalse(shuffledDeck1 == shuffledDeck2)
    }

    @Test
    fun testCreateCardsUsedInHand() {
        val service = DefaultCardService()
        val fullDeckOfCards = service.createShuffledDeck()
        val (flopCards, turnCard, riverCard, pocketCards) = service.createCardsUsedInHand(fullDeckOfCards, 2)
        assertEquals(2, pocketCards.size)
        assertEquals(fullDeckOfCards[0], pocketCards[0].card1)
        assertEquals(fullDeckOfCards[1], pocketCards[1].card1)
        assertEquals(fullDeckOfCards[2], pocketCards[0].card2)
        assertEquals(fullDeckOfCards[3], pocketCards[1].card2)
        assertEquals(fullDeckOfCards[5], flopCards.card1)
        assertEquals(fullDeckOfCards[6], flopCards.card2)
        assertEquals(fullDeckOfCards[7], flopCards.card3)
        assertEquals(fullDeckOfCards[9], turnCard.card)
        assertEquals(fullDeckOfCards[11], riverCard.card)
    }
}