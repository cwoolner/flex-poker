package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryOpenGameForPlayerRepositoryTest {

    @Autowired
    private lateinit var repository: OpenGameForPlayerRepository

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