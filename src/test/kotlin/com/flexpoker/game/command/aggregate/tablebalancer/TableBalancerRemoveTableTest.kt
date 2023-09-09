package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.createSingleBalancingEvent
import com.flexpoker.game.command.events.TableRemovedEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class TableBalancerRemoveTableTest {

    @Test
    fun testSubjectTableIsEmpty() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 0, 2)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableRemovedEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as TableRemovedEvent).tableId)
    }

    @Test
    fun testSingleOtherEmptyTable() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 0)
        val otherTableId = tableToPlayersMap.keys.first { it != subjectTableId }
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableRemovedEvent::class.java, event.get().javaClass)
        assertEquals(otherTableId, (event.get() as TableRemovedEvent).tableId)
    }

    @Test
    fun testMultipleOtherEmptyTables() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 0, 0)
        val otherTableIds = tableToPlayersMap.keys.filter { it != subjectTableId }.toSet()
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableRemovedEvent::class.java, event.get().javaClass)
        assertTrue(otherTableIds.contains((event.get() as TableRemovedEvent).tableId))
    }

    @Test
    fun testNoEmptyOtherwiseBalancedTables() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 2)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

}