package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.createSingleBalancingEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class TableBalancerNothingToDoTest {

    @Test
    fun testSingleTableTwoPlayers() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(), tableToPlayersMap,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testTwoTablesTwoPlayersEach() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 2)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, emptySet(),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testTwoTablesOneAlreadyPausedWaitingForMerge() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 1)
        val otherTableId = tableToPlayersMap.keys.first { it != subjectTableId }
        val event = createSingleBalancingEvent(UUID.randomUUID(), 2, subjectTableId, setOf(otherTableId),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testTwoTablesWithinOneOfEachOtherAndUnderMergeThreshold() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 6, 7)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 9, subjectTableId, emptySet(),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesPerfectlyInBalanceAndUnderMergeThreshold() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 7, 7, 7)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 9, subjectTableId, emptySet(),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesAllAtMax() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 6, 6, 6)
        val event = createSingleBalancingEvent(UUID.randomUUID(), 6, subjectTableId, emptySet(),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsMediumTableSmallestPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 8, 7, 9)
        val smallestOtherTableId = tableToPlayersMap.entries.first { it.value.size == 7 }.key
        val event = createSingleBalancingEvent(UUID.randomUUID(), 9, subjectTableId, setOf(smallestOtherTableId),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesAllImbalancedCantMergeSubjectIsMediumTableSmallestPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 7, 6, 9)
        val smallestOtherTableId = tableToPlayersMap.entries.first { it.value.size == 6 }.key
        val event = createSingleBalancingEvent(UUID.randomUUID(), 9, subjectTableId, setOf(smallestOtherTableId),
            tableToPlayersMap, TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

}