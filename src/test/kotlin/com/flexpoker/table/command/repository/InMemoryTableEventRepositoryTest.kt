package com.flexpoker.table.command.repository

import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@InMemoryTestClass
class InMemoryTableEventRepositoryTest {

    @Autowired
    private lateinit var repository: TableEventRepository

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
