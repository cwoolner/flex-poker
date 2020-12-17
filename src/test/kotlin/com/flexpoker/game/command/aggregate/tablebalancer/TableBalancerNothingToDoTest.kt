package com.flexpoker.game.command.aggregate.tablebalancer

import com.flexpoker.game.command.aggregate.TableBalancer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.UUID

class TableBalancerNothingToDoTest {

    @Test
    fun testSingleTableTwoPlayers() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testTwoTablesTwoPlayersEach() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 2)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testTwoTablesOneAlreadyPausedWaitingForMerge() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 2, 1)
        val otherTableId = tableToPlayersMap.keys.first { it != subjectTableId }
        val tableBalancer = TableBalancer(UUID.randomUUID(), 2)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, setOf(otherTableId), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testTwoTablesWithinOneOfEachOtherAndUnderMergeThreshold() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 6, 7)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesPerfectlyInBalanceAndUnderMergeThreshold() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 7, 7, 7)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesAllAtMax() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 6, 6, 6)
        val tableBalancer = TableBalancer(UUID.randomUUID(), 6)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, emptySet(), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsMediumTableSmallestPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 8, 7, 9)
        val smallestOtherTableId = tableToPlayersMap.entries.first { it.value.size == 7 }.key
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, setOf(smallestOtherTableId), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap)
        )
        assertFalse(event.isPresent)
    }

    @Test
    fun testThreeTablesAllImbalancedCantMergeSubjectIsMediumTableSmallestPaused() {
        val subjectTableId = UUID.randomUUID()
        val tableToPlayersMap = TableBalancerTestUtils.createTableToPlayersMap(subjectTableId, 7, 6, 9)
        val smallestOtherTableId = tableToPlayersMap.entries.first { it.value.size == 6 }.key
        val tableBalancer = TableBalancer(UUID.randomUUID(), 9)
        val event = tableBalancer.createSingleBalancingEvent(subjectTableId, setOf(smallestOtherTableId), tableToPlayersMap as Map<UUID, MutableSet<UUID>>,
            TableBalancerTestUtils.createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap))
        assertFalse(event.isPresent)
    }
}