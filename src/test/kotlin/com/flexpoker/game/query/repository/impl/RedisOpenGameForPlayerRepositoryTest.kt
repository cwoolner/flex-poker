package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisOpenGameForPlayerRepositoryTest {

    @Autowired
    private lateinit var repository: OpenGameForPlayerRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

    @Test
    fun testFetchAllOpenGamesForPlayerNoGames() {
        sharedTestFetchAllOpenGamesForPlayerNoGames(repository)
    }

    @Test
    fun testFetchAllOpenGamesOrdered() {
        sharedTestFetchAllOpenGamesOrdered(repository)
    }

    @Test
    fun testDeleteOpenGameForPlayer() {
        sharedTestDeleteOpenGameForPlayer(repository)
    }

    @Test
    fun testAddOpenGameForUser() {
        sharedTestAddOpenGameForUser(repository)
    }

    @Test
    fun testChangeGameStage() {
        sharedTestChangeGameStage(repository)
    }

    @Test
    fun testAssignTableToOpenGame() {
        sharedTestAssignTableToOpenGame(repository)
    }

}