package com.flexpoker.table.query.repository.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.model.card.CardSuit;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public class InMemoryCardsUsedInHandRepositoryTest {

    @Test
    public void testSaveFlopCards() {
        InMemoryCardsUsedInHandRepository repository = new InMemoryCardsUsedInHandRepository();
        UUID handId = UUID.randomUUID();
        Card card1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        Card card2 = new Card(1, CardRank.THREE, CardSuit.HEARTS);
        Card card3 = new Card(2, CardRank.FOUR, CardSuit.HEARTS);
        repository.saveFlopCards(handId, new FlopCards(card1, card2, card3));
        assertEquals(card1, repository.fetchFlopCards(handId).getCard1());
        assertEquals(card2, repository.fetchFlopCards(handId).getCard2());
        assertEquals(card3, repository.fetchFlopCards(handId).getCard3());
    }

    @Test
    public void testSaveTurnCard() {
        InMemoryCardsUsedInHandRepository repository = new InMemoryCardsUsedInHandRepository();
        UUID handId = UUID.randomUUID();
        Card card1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        repository.saveTurnCard(handId, new TurnCard(card1));
        assertEquals(card1, repository.fetchTurnCard(handId).getCard());
    }

    @Test
    public void testSaveRiverCard() {
        InMemoryCardsUsedInHandRepository repository = new InMemoryCardsUsedInHandRepository();
        UUID handId = UUID.randomUUID();
        Card card1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        repository.saveRiverCard(handId, new RiverCard(card1));
        assertEquals(card1, repository.fetchRiverCard(handId).getCard());
    }

    @Test
    public void testSavePocketCards() {
        InMemoryCardsUsedInHandRepository repository = new InMemoryCardsUsedInHandRepository();
        UUID handId = UUID.randomUUID();

        UUID player1Id = UUID.randomUUID();
        PocketCards pocketCards1 = new PocketCards(
                new Card(0, CardRank.TWO, CardSuit.HEARTS),
                new Card(1, CardRank.THREE, CardSuit.HEARTS));

        UUID player2Id = UUID.randomUUID();
        PocketCards pocketCards2 = new PocketCards(
                new Card(0, CardRank.FOUR, CardSuit.HEARTS),
                new Card(1, CardRank.FIVE, CardSuit.HEARTS));

        Map<UUID, PocketCards> playerToPocketCardsMap = new HashMap<>();
        playerToPocketCardsMap.put(player1Id, pocketCards1);
        playerToPocketCardsMap.put(player2Id, pocketCards2);
        repository.savePocketCards(handId, playerToPocketCardsMap);

        assertEquals(pocketCards1, repository.fetchPocketCards(handId, player1Id));
        assertEquals(pocketCards2, repository.fetchPocketCards(handId, player2Id));
    }

}
