package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.TableBalancer
import com.flexpoker.game.command.events.TableRemovedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerRemoveTableTest {

    @Test
    fun testSubjectTableIsEmpty() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 0, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableRemovedEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as TableRemovedEvent).tableId)
    }

    @Test
    fun testSingleOtherEmptyTable() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 0)
        val otherTableId = tableToPlayersMap.keys.first { it != subjectTableId }
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableRemovedEvent::class.java, event.get().javaClass)
        assertEquals(otherTableId, (event.get() as TableRemovedEvent).tableId)
    }

    @Test
    fun testMultipleOtherEmptyTables() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 0, 0)
        val otherTableIds = tableToPlayersMap.keys.filter { it != subjectTableId }.toSet()
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableRemovedEvent::class.java, event.get().javaClass)
        assertTrue(otherTableIds.contains((event.get() as TableRemovedEvent).tableId))
    }

    @Test
    fun testNoEmptyOtherwiseBalancedTables() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

}