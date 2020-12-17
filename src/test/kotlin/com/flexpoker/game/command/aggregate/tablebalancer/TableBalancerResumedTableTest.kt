package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.TableBalancer
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerResumedTableTest {

    @Test
    fun testOneBalancedTableThatIsPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, setOf(subjectTableId), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableResumedAfterBalancingEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as TableResumedAfterBalancingEvent).tableId)
    }

    @Test
    fun testTwoBalancedTablesOnePaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 8, 8)
        val otherTableId = tableToPlayersMap.keys.first { it != subjectTableId }
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, setOf(otherTableId), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableResumedAfterBalancingEvent::class.java, event.get().javaClass)
        assertEquals(otherTableId, (event.get() as TableResumedAfterBalancingEvent).tableId)
    }

}