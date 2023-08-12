package com.flexpoker.table.command.repository

import com.flexpoker.test.util.RedisTestClass
import com.redis.testcontainers.RedisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

@RedisTestClass
class RedisTableEventRepositoryTest {

    @Autowired
    private lateinit var repository: TableEventRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = RedisContainer(DockerImageName.parse("redis:7.0.11-alpine3.18"))
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
