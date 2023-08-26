package com.flexpoker.game.query.repository.impl

import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

@InMemoryTestClass
class InMemoryGameListRepositoryTest {

    @Autowired
    private lateinit var repository: GameListRepository

    @Test
    @DirtiesContext
    fun testSaveNew() {
        sharedTestSaveNew(repository)
    }

    @Test
    @DirtiesContext
    fun testFetchAllEmpty() {
        sharedTestFetchAllEmpty(repository)
    }

    @Test
    @DirtiesContext
    fun testFetchAllTwoSaved() {
        sharedTestFetchAllTwoSaved(repository)
    }

    @Test
    @DirtiesContext
    fun testIncrementRegisteredPlayers() {
        sharedTestIncrementRegisteredPlayers(repository)
    }

    @Test
    @DirtiesContext
    fun testFetchGameName() {
        sharedTestFetchGameName(repository)
    }

    @Test
    @DirtiesContext
    fun testChangeGameStage() {
        sharedTestChangeGameStage(repository)
    }

}