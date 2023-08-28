package com.flexpoker.chat.repository

import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.test.annotation.DirtiesContext
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisChatRepositoryTest {

    @Autowired
    private lateinit var repository: ChatRepository

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
    @DirtiesContext
    fun testSaveChatMessage() {
        sharedTestSaveChatMessage(repository)
    }

    @Test
    @DirtiesContext
    fun testFetchAllTypesEmpty() {
        sharedTestFetchAllTypesEmpty(repository)
    }

    @Test
    @DirtiesContext
    fun testFetchAllTypes() {
        sharedTestFetchAllTypes(repository)
    }

}