package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.table.command.aggregate.DefaultRandomNumberGenerator
import com.flexpoker.table.command.aggregate.eventproducers.createTable
import com.flexpoker.table.command.aggregate.testhelpers.TestRandomNumberGenerator
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNewEvents
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.HashSet
import java.util.UUID

class CreateNewTableTest {

    @Test
    fun testCreateNewTestSuccess() {
        val tableId = UUID.randomUUID()
        val playerIds = setOf(UUID.randomUUID(), UUID.randomUUID())
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6)
        val events = createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
            command.playerIds, TestRandomNumberGenerator(2))
        verifyNewEvents(tableId, events, TableCreatedEvent::class.java)
        assertEquals(setOf(2, 3),
            (events[0] as TableCreatedEvent).seatPositionToPlayerMap.filterValues { it != null }.keys.toSet())
    }

    @Test
    fun testPlayerListExactSucceeds() {
        val tableId = UUID.randomUUID()
        val playerIds = setOf(UUID.randomUUID(), UUID.randomUUID())
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2)
        val events = createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
            command.playerIds, TestRandomNumberGenerator(0))
        verifyNewEvents(tableId, events, TableCreatedEvent::class.java)
        assertEquals(setOf(0, 1),
            (events[0] as TableCreatedEvent).seatPositionToPlayerMap.filterValues { it != null }.keys.toSet())
    }

    @Test
    fun testCantCreateATableGreaterThanMaxSize() {
        val playerIds = setOf(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val command = CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 2)
        assertThrows(IllegalArgumentException::class.java) {
            createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
                command.playerIds, DefaultRandomNumberGenerator())
        }
    }

    @Test
    fun testCantCreateATableWithOnlyOnePlayer() {
        val tableId = UUID.randomUUID()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2)
        assertThrows(IllegalArgumentException::class.java) {
            createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
                command.playerIds, DefaultRandomNumberGenerator())
        }
    }

}