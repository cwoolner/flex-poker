package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.DefaultTableFactory
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.HashSet
import java.util.UUID

class CreateNewTableTest {

    @Test
    fun testCreateNewTestSuccess() {
        val tableId = UUID.randomUUID()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6)
        val table = DefaultTableFactory().createNew(command)
        verifyAppliedAndNewEventsForAggregate(table, TableCreatedEvent::class.java)
    }

    @Test
    fun testCantCreateATableGreaterThanMaxSize() {
        val tableId = UUID.randomUUID()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2)
        assertThrows(FlexPokerException::class.java) { DefaultTableFactory().createNew(command) }
    }

    @Test
    fun testCantCreateATableWithOnlyOnePlayer() {
        val tableId = UUID.randomUUID()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2)
        assertThrows(FlexPokerException::class.java) { DefaultTableFactory().createNew(command) }
    }

}