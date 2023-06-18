package com.flexpoker.table.command.repository

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InMemoryTableEventRepositoryTest {

    @Test
    fun testSetEventVersionsAndSaveEmptyList() {
        val repository = InMemoryTableEventRepository()
        val eventsWithVersions = repository.setEventVersionsAndSave(0, emptyList())
        assertTrue(eventsWithVersions.isEmpty())
    }

}