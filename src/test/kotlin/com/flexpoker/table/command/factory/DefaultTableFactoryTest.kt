package com.flexpoker.table.command.factory

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.DefaultTableFactory
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import java.util.ArrayList
import java.util.HashSet
import java.util.UUID

class DefaultTableFactoryTest {

    @Test
    fun testCreateNew() {
        val sut = DefaultTableFactory()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 6)
        val table = sut.createNew(command)
        assertNotNull(table)
        assertFalse(table.fetchAppliedEvents().isEmpty())
        assertFalse(table.fetchNewEvents().isEmpty())
    }

    @Test
    fun testPlayerListToLongFails() {
        val sut = DefaultTableFactory()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 2)
        assertThrows(FlexPokerException::class.java) { sut.createNew(command) }
    }

    @Test
    fun testPlayerListExactSucceeds() {
        val sut = DefaultTableFactory()
        val playerIds = HashSet<UUID>()
        playerIds.add(UUID.randomUUID())
        playerIds.add(UUID.randomUUID())
        val command = CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 2)
        val table = sut.createNew(command)
        assertNotNull(table)
        assertFalse(table.fetchAppliedEvents().isEmpty())
        assertFalse(table.fetchNewEvents().isEmpty())
    }

    @Test
    fun testCreateFrom() {
        val sut = DefaultTableFactory()
        val events = ArrayList<TableEvent>()
        events.add(TableCreatedEvent(UUID.randomUUID(), UUID.randomUUID(), 6, HashTreePMap.empty(),1500))
        val table = sut.createFrom(events)
        assertNotNull(table)
        assertFalse(table.fetchAppliedEvents().isEmpty())
        assertTrue(table.fetchNewEvents().isEmpty())
    }

}