package com.flexpoker.table.command.repository

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.util.toPMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.Collections
import java.util.UUID

fun sharedTestFetchAll(repository: TableEventRepository) {
    val tableId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0,
        listOf(
            TableCreatedEvent(tableId, UUID.randomUUID(), 2, mapOf(0 to UUID.randomUUID()).toPMap(), 1500)
        ))
    val events = repository.fetchAll(tableId)
    assertEquals(1, events.size)
}

fun sharedTestSetEventVersionsAndSaveBadBasedOnVersion1(repository: TableEventRepository) {
    val tableId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    assertThrows(FlexPokerException::class.java) {
        repository.setEventVersionsAndSave(1, listOf(CardsShuffledEvent(tableId, gameId, emptyList())))
    }
}

fun sharedTestSetEventVersionsAndSaveTwoFakeEventsInOneRequest(repository: TableEventRepository) {
    val tableId = UUID.randomUUID()
    val gameId = UUID.randomUUID()
    repository.setEventVersionsAndSave(0,
        listOf(
            TableCreatedEvent(tableId, gameId, 2, mapOf(0 to UUID.randomUUID()).toPMap(), 1500)
        ))
    val eventsWithVersions = repository.setEventVersionsAndSave(1, listOf(CardsShuffledEvent(tableId, gameId, emptyList()), CardsShuffledEvent(tableId, gameId, emptyList())))
    assertEquals(2, eventsWithVersions[0].version)
    assertEquals(3, eventsWithVersions[1].version)

    assertEquals(3, repository.fetchAll(tableId).size)
    assertEquals(1, repository.fetchAll(tableId)[0].version)
    assertEquals(TableCreatedEvent::class.java, repository.fetchAll(tableId)[0].javaClass)
    assertEquals(2, repository.fetchAll(tableId)[1].version)
    assertEquals(CardsShuffledEvent::class.java, repository.fetchAll(tableId)[1].javaClass)
    assertEquals(3, repository.fetchAll(tableId)[2].version)
    assertEquals(CardsShuffledEvent::class.java, repository.fetchAll(tableId)[2].javaClass)
}

fun sharedTestSetEventVersionsAndSaveEmptyList(repository: TableEventRepository) {
    val eventsWithVersions = repository.setEventVersionsAndSave(0, emptyList())
    assertTrue(eventsWithVersions.isEmpty())
    assertTrue(eventsWithVersions !== Collections.emptyList<GameEvent>())
}
