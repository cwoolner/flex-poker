package com.flexpoker.table.command.repository

import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisTableEventRepositoryTest {

    @Autowired
    private lateinit var repository: TableEventRepository

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
    fun testSetEventVersionsAndSaveBadBasedOnVersion1() {
        sharedTestSetEventVersionsAndSaveBadBasedOnVersion1(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveTwoJoinsInOneRequest() {
        sharedTestSetEventVersionsAndSaveTwoFakeEventsInOneRequest(repository)
    }

    @Test
    fun testSetEventVersionsAndSaveEmptyList() {
        sharedTestSetEventVersionsAndSaveEmptyList(repository)
    }

}
