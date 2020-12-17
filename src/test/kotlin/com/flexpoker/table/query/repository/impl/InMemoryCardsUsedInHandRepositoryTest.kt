package com.flexpoker.table.query.repository.impl

import com.flexpoker.table.command.Card
import com.flexpoker.table.command.CardRank
import com.flexpoker.table.command.CardSuit
import com.flexpoker.table.command.FlopCards
import com.flexpoker.table.command.PocketCards
import com.flexpoker.table.command.RiverCard
import com.flexpoker.table.command.TurnCard
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.HashMap
import java.util.UUID

class InMemoryCardsUsedInHandRepositoryTest {

    @Test
    fun testSaveFlopCards() {
        val repository = InMemoryCardsUsedInHandRepository()
        val handId = UUID.randomUUID()
        val card1 = Card(0, CardRank.TWO, CardSuit.HEARTS)
        val card2 = Card(1, CardRank.THREE, CardSuit.HEARTS)
        val card3 = Card(2, CardRank.FOUR, CardSuit.HEARTS)
        repository.saveFlopCards(handId, FlopCards(card1, card2, card3))
        assertEquals(card1, repository.fetchFlopCards(handId).card1)
        assertEquals(card2, repository.fetchFlopCards(handId).card2)
        assertEquals(card3, repository.fetchFlopCards(handId).card3)
    }

    @Test
    fun testSaveTurnCard() {
        val repository = InMemoryCardsUsedInHandRepository()
        val handId = UUID.randomUUID()
        val card1 = Card(0, CardRank.TWO, CardSuit.HEARTS)
        repository.saveTurnCard(handId, TurnCard(card1))
        assertEquals(card1, repository.fetchTurnCard(handId).card)
    }

    @Test
    fun testSaveRiverCard() {
        val repository = InMemoryCardsUsedInHandRepository()
        val handId = UUID.randomUUID()
        val card1 = Card(0, CardRank.TWO, CardSuit.HEARTS)
        repository.saveRiverCard(handId, RiverCard(card1))
        assertEquals(card1, repository.fetchRiverCard(handId).card)
    }

    @Test
    fun testSavePocketCards() {
        val repository = InMemoryCardsUsedInHandRepository()
        val handId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val pocketCards1 = PocketCards(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(1, CardRank.THREE, CardSuit.HEARTS)
        )
        val player2Id = UUID.randomUUID()
        val pocketCards2 = PocketCards(
            Card(0, CardRank.FOUR, CardSuit.HEARTS),
            Card(1, CardRank.FIVE, CardSuit.HEARTS)
        )
        val playerToPocketCardsMap = HashMap<UUID, PocketCards>()
        playerToPocketCardsMap[player1Id] = pocketCards1
        playerToPocketCardsMap[player2Id] = pocketCards2
        repository.savePocketCards(handId, playerToPocketCardsMap)
        assertEquals(pocketCards1, repository.fetchPocketCards(handId, player1Id))
        assertEquals(pocketCards2, repository.fetchPocketCards(handId, player2Id))
    }

    @Test
    fun testFetchAllPocketCardsForUser() {
        val repository = InMemoryCardsUsedInHandRepository()
        val pocketCards1 = PocketCards(
            Card(0, CardRank.TWO, CardSuit.HEARTS),
            Card(1, CardRank.THREE, CardSuit.HEARTS)
        )
        val pocketCards2 = PocketCards(
            Card(0, CardRank.THREE, CardSuit.HEARTS),
            Card(1, CardRank.FOUR, CardSuit.HEARTS)
        )
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        var playerToPocketCardsMap = HashMap<UUID, PocketCards>()
        playerToPocketCardsMap[player1Id] = pocketCards1
        playerToPocketCardsMap[player2Id] = pocketCards2
        val handId1 = UUID.randomUUID()
        repository.savePocketCards(handId1, playerToPocketCardsMap)
        playerToPocketCardsMap = HashMap()
        playerToPocketCardsMap[player1Id] = pocketCards2
        val handId2 = UUID.randomUUID()
        repository.savePocketCards(handId2, playerToPocketCardsMap)
        val player1PocketCards = repository.fetchAllPocketCardsForUser(player1Id)
        val player2PocketCards = repository.fetchAllPocketCardsForUser(player2Id)

        assertEquals(2, player1PocketCards.size)
        assertTrue(player1PocketCards.containsKey(handId1))
        assertTrue(player1PocketCards.containsKey(handId2))
        assertEquals(1, player2PocketCards.size)
        assertTrue(player2PocketCards.containsKey(handId1))
    }

}