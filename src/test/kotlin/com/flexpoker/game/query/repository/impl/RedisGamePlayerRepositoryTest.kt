package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.GamePlayerRepository
import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisGamePlayerRepositoryTest {

    @Autowired
    private lateinit var repository: GamePlayerRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

    @Test
    fun testAddPlayerToGame() {
        sharedTestAddPlayerToGame(repository)
    }

    @Test
    fun testAddPlayerToGameDuplicate() {
        sharedTestAddPlayerToGameDuplicate(repository)
    }

    @Test
    fun testFetchAllPlayerIdsForGameNoneAdded() {
        sharedTestFetchAllPlayerIdsForGameNoneAdded(repository)
    }

}