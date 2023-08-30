package com.flexpoker.table.query.repository.impl

import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisCardsUsedInHandRepositoryTest {

    @Autowired
    private lateinit var repository: CardsUsedInHandRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

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
