package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.createSingleBalancingEvent
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerResumedTableTest {

    @Test
    fun testOneBalancedTableThatIsPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, setOf(subjectTableId),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableResumedAfterBalancingEvent::class.java, event.get().javaClass)
        assertEquals(subjectTableId, (event.get() as TableResumedAfterBalancingEvent).tableId)
    }

    @Test
    fun testTwoBalancedTablesOnePaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 8, 8)
        val otherTableId = tableToPlayersMap.keys.first { it != subjectTableId }
        val event = createSingleBalancingEvent(UUID.randomUUID(), 9, subjectTableId, setOf(otherTableId),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertEquals(TableResumedAfterBalancingEvent::class.java, event.get().javaClass)
        assertEquals(otherTableId, (event.get() as TableResumedAfterBalancingEvent).tableId)
    }

}