package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.GamePlayerRepository
import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryGamePlayerRepositoryTest {

    @Autowired
    private lateinit var repository: GamePlayerRepository

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
