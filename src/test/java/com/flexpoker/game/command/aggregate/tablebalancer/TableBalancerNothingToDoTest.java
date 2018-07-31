package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;

public class TableBalancerNothingToDoTest {

    @Test
    void testSingleTableTwoPlayers() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 2);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testTwoTablesTwoPlayersEach() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 2, 2);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testTwoTablesOneAlreadyPausedWaitingForMerge() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 2, 1);
        var otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.singleton(otherTableId),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testTwoTablesWithinOneOfEachOtherAndUnderMergeThreshold() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 6, 7);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testThreeTablesPerfectlyInBalanceAndUnderMergeThreshold() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 7, 7, 7);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testThreeTablesAllAtMax() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 6, 6, 6);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 6);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsMediumTableSmallestPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 8, 7, 9);
        var smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 7).findFirst().get()
                .getKey();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId,
                Collections.singleton(smallestOtherTableId), tableToPlayersMap,
                createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

    @Test
    void testThreeTablesAllImbalancedCantMergeSubjectIsMediumTableSmallestPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 7, 6, 9);
        var smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 6).findFirst().get()
                .getKey();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId,
                Collections.singleton(smallestOtherTableId), tableToPlayersMap,
                createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertFalse(event.isPresent());
    }

}
