package com.flexpoker.table.query.repository.impl

import com.flexpoker.table.query.repository.TableRepository
import com.flexpoker.test.util.RedisTestClass
import com.flexpoker.test.util.redisContainer
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container

@RedisTestClass
class RedisTableRepositoryTest {

    @Autowired
    private lateinit var repository: TableRepository

    companion object {
        @Container
        @ServiceConnection
        val redisContainer = redisContainer()
    }

    @Test
    fun testFetchNonExistentTable() {
        sharedTestFetchNonExistentTable(repository)
    }

    @Test
    fun testFetchExistingTable() {
        sharedTestFetchExistingTable(repository)
    }

    @Test
    fun testSaveIncrementingVersions() {
        sharedTestSaveIncrementingVersions(repository)
    }

    @Test
    fun testSaveDecrementingVersions() {
        sharedTestSaveDecrementingVersions(repository)
    }

    @Test
    fun testSaveDuplicateVersionsDoNotOverwrite() {
        sharedTestSaveDuplicateVersionsDoNotOverwrite(repository)
    }

    @RepeatedTest(100)
    @Throws(InterruptedException::class)
    fun testSaveMultithreadVersionsWithTwoDifferentTables() {
        sharedTestSaveMultithreadVersionsWithTwoDifferentTables(repository)
    }

    @RepeatedTest(100)
    @Throws(InterruptedException::class)
    fun testFetchMultithreaded() {
        sharedTestFetchMultithreaded(repository)
    }

}