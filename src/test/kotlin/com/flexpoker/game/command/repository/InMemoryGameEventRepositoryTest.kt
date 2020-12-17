package com.flexpoker.game.command.repository

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class InMemoryGameEventRepositoryTest {

    @Test
    fun testFetchAll() {
        val repository = InMemoryGameEventRepository()
        val gameId = UUID.randomUUID()
        repository.setEventVersionsAndSave(0,
            listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
                10, 10)))
        val events = repository.fetchAll(gameId)
        assertEquals(1, events.size)
    }

    @Test
    fun testFetchGameCreatedEventSuccess() {
        val repository = InMemoryGameEventRepository()
        val gameId = UUID.randomUUID()
        repository.setEventVersionsAndSave(0,
            listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
                10, 10)))
        repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        val event = repository.fetchGameCreatedEvent(gameId)
        assertNotNull(event)
    }

    @Test
    fun testFetchGameCreatedEventFail() {
        val repository = InMemoryGameEventRepository()
        val gameId = UUID.randomUUID()
        repository.setEventVersionsAndSave(0, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        val event = repository.fetchGameCreatedEvent(gameId)
        assertNull(event)
    }

    @Test
    fun testSetEventVersionsAndSaveBadBasedOnVersion1() {
        val repository = InMemoryGameEventRepository()
        val gameId = UUID.randomUUID()
        assertThrows(FlexPokerException::class.java) {
            repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        }
    }

    @Test
    fun testSetEventVersionsAndSaveTwoJoinsSameTime() {
        val repository = InMemoryGameEventRepository()
        val gameId = UUID.randomUUID()
        repository.setEventVersionsAndSave(0,
            listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
                10, 10)))
        repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        assertThrows(FlexPokerException::class.java) {
            repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        }
    }

    @Test
    fun testSetEventVersionsAndSaveTwoJoinsSeparateTime() {
        val repository = InMemoryGameEventRepository()
        val gameId = UUID.randomUUID()
        repository.setEventVersionsAndSave(0,
            listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
                10, 10)))
        repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        repository.setEventVersionsAndSave(2, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
        assertEquals(3, repository.fetchAll(gameId).size)
        assertEquals(1, repository.fetchAll(gameId)[0].version)
        assertEquals(GameCreatedEvent::class.java, repository.fetchAll(gameId)[0].javaClass)
        assertEquals(2, repository.fetchAll(gameId)[1].version)
        assertEquals(GameJoinedEvent::class.java, repository.fetchAll(gameId)[1].javaClass)
        assertEquals(3, repository.fetchAll(gameId)[2].version)
        assertEquals(GameJoinedEvent::class.java, repository.fetchAll(gameId)[2].javaClass)
    }

}