package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisGameListRepositoryTest {

    @Autowired
    private lateinit var repository: GameListRepository

    @Autowired
    private lateinit var connectionFactory: RedisConnectionFactory

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

    @BeforeEach
    fun beforeEach() {
        connectionFactory.connection.serverCommands().flushAll()
    }

    @Test
    fun testSaveNew() {
        sharedTestSaveNew(repository)
    }

    @Test
    fun testFetchAllEmpty() {
        sharedTestFetchAllEmpty(repository)
    }

    @Test
    fun testFetchAllTwoSaved() {
        sharedTestFetchAllTwoSaved(repository)
    }

    @Test
    fun testIncrementRegisteredPlayers() {
        sharedTestIncrementRegisteredPlayers(repository)
    }

    @Test
    fun testFetchGameName() {
        sharedTestFetchGameName(repository)
    }

    @Test
    fun testChangeGameStage() {
        sharedTestChangeGameStage(repository)
    }

}