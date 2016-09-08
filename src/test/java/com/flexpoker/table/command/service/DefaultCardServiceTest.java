package com.flexpoker.table.command.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;

public class DefaultCardServiceTest {

    @Test
    public void testCreateShuffledDeck() {
        DefaultCardService service = new DefaultCardService();
        List<Card> shuffledDeck = service.createShuffledDeck();
        assertEquals(52, shuffledDeck.size());
    }

    @Test
    public void testShuffledDecksAreDifferent() {
        DefaultCardService service = new DefaultCardService();
        List<Card> shuffledDeck1 = service.createShuffledDeck();
        List<Card> shuffledDeck2 = service.createShuffledDeck();

        assertFalse(shuffledDeck1 == shuffledDeck2);
        assertFalse(shuffledDeck1.equals(shuffledDeck2));
    }

    @Test
    public void testCreateCardsUsedInHand() {
        DefaultCardService service = new DefaultCardService();
        List<Card> fullDeckOfCards = service.createShuffledDeck();
        CardsUsedInHand cardsUsedInHand = service.createCardsUsedInHand(fullDeckOfCards , 2);

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
