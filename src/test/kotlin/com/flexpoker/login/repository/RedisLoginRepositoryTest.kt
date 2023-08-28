package com.flexpoker.login.repository

import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisLoginRepositoryTest {

    @Autowired
    private lateinit var repository: LoginRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

    @Test
    fun testLoadUserByUsername() {
        sharedTestLoadUserByUsername(repository)
    }

    @Test
    fun testFetchAggregateIdByUsername() {
        sharedTestFetchAggregateIdByUsername(repository)
    }

    @Test
    fun testFetchUsernameByAggregateId() {
        sharedTestFetchUsernameByAggregateId(repository)
    }

    @Test
    fun testSaveAggregateIdAndUsername() {
        sharedTestSaveAggregateIdAndUsername(repository)
    }

    @Test
    fun testSaveUsernameAndPassword() {
        sharedTestSaveUsernameAndPassword(repository)
    }

}