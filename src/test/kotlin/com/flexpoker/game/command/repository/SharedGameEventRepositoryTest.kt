package com.flexpoker.game.command.repository

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.Collections
import java.util.UUID

fun sharedTestFetchAll(repository: GameEventRepository) {
    val gameId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0,
        listOf(
            GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
            10, 10)
        ))
    val events = repository.fetchAll(gameId)
    assertEquals(1, events.size)
}

fun sharedTestFetchGameCreatedEventSuccess(repository: GameEventRepository) {
    val gameId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0,
        listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
            10, 10)))
    repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
    val event = repository.fetchGameCreatedEvent(gameId)
    assertNotNull(event)
}

fun sharedTestFetchGameCreatedEventFail(repository: GameEventRepository) {
    val gameId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
    assertThrows(NoSuchElementException::class.java) { repository.fetchGameCreatedEvent(gameId) }
}

fun sharedTestSetEventVersionsAndSaveBadBasedOnVersion1(repository: GameEventRepository) {
    val gameId = UUID.randomUUID()
    assertThrows(FlexPokerException::class.java) {
        repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
    }
}

fun sharedTestSetEventVersionsAndSaveTwoJoinsSameTime(repository: GameEventRepository) {
    val gameId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0,
        listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
            10, 10)))
    repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
    assertThrows(FlexPokerException::class.java) {
        repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID())))
    }
}

fun sharedTestSetEventVersionsAndSaveTwoJoinsSeparateTime(repository: GameEventRepository) {
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

fun sharedTestSetEventVersionsAndSaveTwoJoinsInOneRequest(repository: GameEventRepository) {
    val gameId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0,
        listOf(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(),
            10, 10)))
    val eventsWithVersions = repository.setEventVersionsAndSave(1, listOf(GameJoinedEvent(gameId, UUID.randomUUID()), GameJoinedEvent(gameId, UUID.randomUUID())))
    assertEquals(2, eventsWithVersions[0].version)
    assertEquals(3, eventsWithVersions[1].version)

    assertEquals(3, repository.fetchAll(gameId).size)
    assertEquals(1, repository.fetchAll(gameId)[0].version)
    assertEquals(GameCreatedEvent::class.java, repository.fetchAll(gameId)[0].javaClass)
    assertEquals(2, repository.fetchAll(gameId)[1].version)
    assertEquals(GameJoinedEvent::class.java, repository.fetchAll(gameId)[1].javaClass)
    assertEquals(3, repository.fetchAll(gameId)[2].version)
    assertEquals(GameJoinedEvent::class.java, repository.fetchAll(gameId)[2].javaClass)
}

fun sharedTestSetEventVersionsAndSaveEmptyList(repository: GameEventRepository) {
    val eventsWithVersions = repository.setEventVersionsAndSave(0, emptyList())
    assertTrue(eventsWithVersions.isEmpty())
    assertTrue(eventsWithVersions !== Collections.emptyList<GameEvent>())
}
