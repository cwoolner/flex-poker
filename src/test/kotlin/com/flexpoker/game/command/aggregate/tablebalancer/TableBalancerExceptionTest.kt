package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.aggregate.TableBalancer
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerExceptionTest {

    @Test
    fun testSingleTableOnePlayer() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        assertThrows(FlexPokerException::class.java) {
            tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
                TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        }
    }

    @Test
    fun testTwoTablesOnePlayer() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 1, 0)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        assertThrows(FlexPokerException::class.java) {
            tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
                TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        }
    }

    @Test
    fun testAllTablesEmpty() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 0, 0)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        assertThrows(FlexPokerException::class.java) {
            tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
                TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        }
    }

}