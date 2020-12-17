package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.TableBalancer
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerPausedTableTest {

    @Test
    fun testTwoTablesOneAndTwoPlayersCantMerge() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TablePausedForBalancingEvent::class.java, event.get().javaClass)
    }

    @Test
    fun testTwoTablesOneAndThreePlayersCantMerge() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 3)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 3)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TablePausedForBalancingEvent::class.java, event.get().javaClass)
    }

    @Test
    fun testTwoTablesImbalancedByTwoCantMergeSubjectIsSmallerTableNoPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 5, 7)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TablePausedForBalancingEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as TablePausedForBalancingEvent).tableId)
    }

    @Test
    fun testTwoTablesImbalancedByTwoCantMergeSubjectIsLargerTableNoPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 7, 5)
        val otherTableId = tableToPlayersMap!!.keys.first { it != subjectTableId }
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(PlayerMovedToNewTableEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as PlayerMovedToNewTableEvent).fromTableId)
        assertEquals(otherTableId, (event.get() as PlayerMovedToNewTableEvent).toTableId)
    }

    @Test
    fun testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsSmallestTableNoPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 7, 8, 9)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TablePausedForBalancingEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as TablePausedForBalancingEvent).tableId)
    }

    @Test
    fun testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsLargestTableNoPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 9, 8, 7)
        val smallestOtherTableId = tableToPlayersMap.entries.first { it.value.size == 7 }.key
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(PlayerMovedToNewTableEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as PlayerMovedToNewTableEvent).fromTableId)
        assertEquals(smallestOtherTableId, (event.get() as PlayerMovedToNewTableEvent).toTableId)
    }

}