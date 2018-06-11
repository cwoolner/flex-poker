package com.flexpoker.table.command.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class DefaultCardServiceTest {

    @Test
    public void testCreateShuffledDeck() {
        var service = new DefaultCardService();
        var shuffledDeck = service.createShuffledDeck();
        assertEquals(52, shuffledDeck.size());
    }

    @Test
    public void testShuffledDecksAreDifferent() {
        var service = new DefaultCardService();
        var shuffledDeck1 = service.createShuffledDeck();
        var shuffledDeck2 = service.createShuffledDeck();

        assertFalse(shuffledDeck1 == shuffledDeck2);
        assertFalse(shuffledDeck1.equals(shuffledDeck2));
    }

    @Test
    public void testCreateCardsUsedInHand() {
        var service = new DefaultCardService();
        var fullDeckOfCards = service.createShuffledDeck();
        var cardsUsedInHand = service.createCardsUsedInHand(fullDeckOfCards , 2);

        assertEquals(2, cardsUsedInHand.getPocketCards().size());

        assertEquals(fullDeckOfCards.get(0), cardsUsedInHand.getPocketCards().get(0).getCard1());
        assertEquals(fullDeckOfCards.get(1), cardsUsedInHand.getPocketCards().get(1).getCard1());
        assertEquals(fullDeckOfCards.get(2), cardsUsedInHand.getPocketCards().get(0).getCard2());
        assertEquals(fullDeckOfCards.get(3), cardsUsedInHand.getPocketCards().get(1).getCard2());

        assertEquals(fullDeckOfCards.get(5), cardsUsedInHand.getFlopCards().getCard1());
        assertEquals(fullDeckOfCards.get(6), cardsUsedInHand.getFlopCards().getCard2());
        assertEquals(fullDeckOfCards.get(7), cardsUsedInHand.getFlopCards().getCard3());
        assertEquals(fullDeckOfCards.get(9), cardsUsedInHand.getTurnCard().getCard());
        assertEquals(fullDeckOfCards.get(11), cardsUsedInHand.getRiverCard().getCard());
    }

}
