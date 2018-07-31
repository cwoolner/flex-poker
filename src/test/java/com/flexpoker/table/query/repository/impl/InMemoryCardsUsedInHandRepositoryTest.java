package com.flexpoker.table.query.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.model.card.CardSuit;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public class InMemoryCardsUsedInHandRepositoryTest {

    @Test
    void testSaveFlopCards() {
        var repository = new InMemoryCardsUsedInHandRepository();
        var handId = UUID.randomUUID();
        var card1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        var card2 = new Card(1, CardRank.THREE, CardSuit.HEARTS);
        var card3 = new Card(2, CardRank.FOUR, CardSuit.HEARTS);
        repository.saveFlopCards(handId, new FlopCards(card1, card2, card3));
        assertEquals(card1, repository.fetchFlopCards(handId).getCard1());
        assertEquals(card2, repository.fetchFlopCards(handId).getCard2());
        assertEquals(card3, repository.fetchFlopCards(handId).getCard3());
    }

    @Test
    void testSaveTurnCard() {
        var repository = new InMemoryCardsUsedInHandRepository();
        var handId = UUID.randomUUID();
        var card1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        repository.saveTurnCard(handId, new TurnCard(card1));
        assertEquals(card1, repository.fetchTurnCard(handId).getCard());
    }

    @Test
    void testSaveRiverCard() {
        var repository = new InMemoryCardsUsedInHandRepository();
        var handId = UUID.randomUUID();
        var card1 = new Card(0, CardRank.TWO, CardSuit.HEARTS);
        repository.saveRiverCard(handId, new RiverCard(card1));
        assertEquals(card1, repository.fetchRiverCard(handId).getCard());
    }

    @Test
    void testSavePocketCards() {
        var repository = new InMemoryCardsUsedInHandRepository();
        var handId = UUID.randomUUID();

        var player1Id = UUID.randomUUID();
        var pocketCards1 = new PocketCards(
                new Card(0, CardRank.TWO, CardSuit.HEARTS),
                new Card(1, CardRank.THREE, CardSuit.HEARTS));

        var player2Id = UUID.randomUUID();
        var pocketCards2 = new PocketCards(
                new Card(0, CardRank.FOUR, CardSuit.HEARTS),
                new Card(1, CardRank.FIVE, CardSuit.HEARTS));

        var playerToPocketCardsMap = new HashMap<UUID, PocketCards>();
        playerToPocketCardsMap.put(player1Id, pocketCards1);
        playerToPocketCardsMap.put(player2Id, pocketCards2);
        repository.savePocketCards(handId, playerToPocketCardsMap);

        assertEquals(pocketCards1, repository.fetchPocketCards(handId, player1Id));
        assertEquals(pocketCards2, repository.fetchPocketCards(handId, player2Id));
    }

    @Test
    void testFetchAllPocketCardsForUser() {
        InMemoryCardsUsedInHandRepository repository = new InMemoryCardsUsedInHandRepository();

        PocketCards pocketCards1 = new PocketCards(
                new Card(0, CardRank.TWO, CardSuit.HEARTS),
                new Card(1, CardRank.THREE, CardSuit.HEARTS));
        PocketCards pocketCards2 = new PocketCards(
                new Card(0, CardRank.THREE, CardSuit.HEARTS),
                new Card(1, CardRank.FOUR, CardSuit.HEARTS));

        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();

        var playerToPocketCardsMap = new HashMap<UUID, PocketCards>();
        playerToPocketCardsMap.put(player1Id, pocketCards1);
        playerToPocketCardsMap.put(player2Id, pocketCards2);
        UUID handId1 = UUID.randomUUID();
        repository.savePocketCards(handId1, playerToPocketCardsMap);

        playerToPocketCardsMap = new HashMap<>();
        playerToPocketCardsMap.put(player1Id, pocketCards2);
        UUID handId2 = UUID.randomUUID();
        repository.savePocketCards(handId2, playerToPocketCardsMap);

        var player1PocketCards = repository.fetchAllPocketCardsForUser(player1Id);
        var player2PocketCards = repository.fetchAllPocketCardsForUser(player2Id);

        assertEquals(2, player1PocketCards.size());
        assertTrue(player1PocketCards.containsKey(handId1));
        assertTrue(player1PocketCards.containsKey(handId2));

        assertEquals(1, player2PocketCards.size());
        assertTrue(player2PocketCards.containsKey(handId1));
    }

}
