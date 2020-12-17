package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.TableBalancer
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerMergeTablesTest {

    @Test
    fun testTwoTablesOneAlreadyPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 1)
        val otherTableId = tableToPlayersMap!!.keys.first { it != subjectTableId }
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, setOf(otherTableId), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(PlayerMovedToNewTableEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as PlayerMovedToNewTableEvent).fromTableId)
    }

    @Test
    fun testTwoTablesReadyToMerge() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 3)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(PlayerMovedToNewTableEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as PlayerMovedToNewTableEvent).fromTableId)
    }

    @Test
    fun testThreeTablesReadyToMerge() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 3, 2)
        val smallestOtherTableId = tableToPlayersMap.entries.first { it.value.size == 2 }.key
        val tableBalancer = TableBalancer(UUID.randomUUID(), 3)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(PlayerMovedToNewTableEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as PlayerMovedToNewTableEvent).fromTableId)
        assertEquals(smallestOtherTableId, (event.get() as PlayerMovedToNewTableEvent).toTableId)
    }

}