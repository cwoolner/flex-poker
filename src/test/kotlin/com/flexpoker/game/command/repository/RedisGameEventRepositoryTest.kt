package com.flexpoker.game.command.repository

import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisGameEventRepositoryTest {

    @Autowired
    private lateinit var repository: GameEventRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

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
