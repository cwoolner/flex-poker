package com.flexpoker.game.command.repository

import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryGameEventRepositoryTest {

    @Autowired
    private lateinit var repository: GameEventRepository

    @Test
    fun testFetchAll() {
        sharedTestFetchAll(repository)
    }

    @Test
    fun testFetchGameCreatedEventSuccess() {
        sharedTestFetchGameCreatedEventSuccess(repository)
    }

    @Test
    fun testFetchGameCreatedEventFail() {
        sharedTestFetchGameCreatedEventFail(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveBadBasedOnVersion1() {
        sharedTestSetEventVersionsAndSaveBadBasedOnVersion1(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveTwoJoinsSameTime() {
        sharedTestSetEventVersionsAndSaveTwoJoinsSameTime(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveTwoJoinsSeparateTime() {
        sharedTestSetEventVersionsAndSaveTwoJoinsSeparateTime(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveTwoJoinsInOneRequest() {
        sharedTestSetEventVersionsAndSaveTwoJoinsInOneRequest(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveEmptyList() {
        sharedTestSetEventVersionsAndSaveEmptyList(repository)
    }

}
