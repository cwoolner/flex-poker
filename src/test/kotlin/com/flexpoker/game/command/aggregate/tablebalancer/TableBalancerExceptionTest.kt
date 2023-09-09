package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.aggregate.createSingleBalancingEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class TableBalancerExceptionTest {

    @Test
    fun testSingleTableOnePlayer() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1)
        assertThrows(FlexPokerException::class.java) {
            createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
                TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        }
    }

    @Test
    fun testTwoTablesOnePlayer() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 0)
        assertThrows(FlexPokerException::class.java) {
            createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
                TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        }
    }

    @Test
    fun testAllTablesEmpty() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 0, 0)
        assertThrows(FlexPokerException::class.java) {
            createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
                TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        }
    }

}