package com.flexpoker.signup.repository

import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisSignUpRepositoryTest {

    @Autowired
    private lateinit var repository: SignUpRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

    @Test
    fun testUsernameExists() {
        sharedTestUsernameExists(repository)
    }

    @Test
    fun testFetchSignUpUser() {
        sharedTestFetchSignUpUser(repository)
    }

    @Test
    fun testSaveSignUpUser() {
        sharedTestSaveSignUpUser(repository)
    }

    @Test
    fun testFindSignUpCodeByUsername() {
        sharedTestFindSignUpCodeByUsername(repository)
    }

    @Test
    fun testFindAggregateIdByUsernameAndSignUpCode() {
        sharedTestFindAggregateIdByUsernameAndSignUpCode(repository)
    }

    @Test
    fun testStoreNewlyConfirmedUsername() {
        sharedTestStoreNewlyConfirmedUsername(repository)
    }

    @Test
    fun testSignUpCodeExists() {
        sharedTestSignUpCodeExists(repository)
    }

    @Test
    fun testStoreSignUpInformation() {
        sharedTestStoreSignUpInformation(repository)
    }

}