package com.flexpoker.login.repository

import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryLoginRepositoryTest {

    @Autowired
    private lateinit var repository: LoginRepository

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