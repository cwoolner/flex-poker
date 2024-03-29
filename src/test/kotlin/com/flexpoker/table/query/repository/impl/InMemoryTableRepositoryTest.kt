package com.flexpoker.table.query.repository.impl

import com.flexpoker.table.query.repository.TableRepository
import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryTableRepositoryTest {

    @Autowired
    private lateinit var repository: TableRepository

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