package com.flexpoker.chat.repository

import com.flexpoker.test.util.InMemoryTestClass
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

@InMemoryTestClass
class InMemoryChatRepositoryTest {

    @Autowired
    private lateinit var repository: ChatRepository

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