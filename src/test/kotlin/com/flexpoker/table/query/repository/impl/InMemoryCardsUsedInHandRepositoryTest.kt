package com.flexpoker.table.query.repository.impl

import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryCardsUsedInHandRepositoryTest {

    @Autowired
    private lateinit var repository: CardsUsedInHandRepository

    @Test
    fun testSaveFlopCards() {
        sharedTestSaveFlopCards(repository)
    }

    @Test
    fun testSaveTurnCard() {
        sharedTestSaveTurnCard(repository)
    }

    @Test
    fun testSaveRiverCard() {
        sharedTestSaveRiverCard(repository)
    }

    @Test
    fun testSavePocketCards() {
        sharedTestSavePocketCards(repository)
    }

    @Test
    fun testFetchAllPocketCardsForUser() {
        sharedTestFetchAllPocketCardsForUser(repository)
    }

    @Test
    fun testRemoveHand() {
        sharedTestRemoveHand(repository)
    }

}
