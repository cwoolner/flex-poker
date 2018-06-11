package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;

public class TableBalancerPausedTableTest {

    @Test
    public void testTwoTablesOneAndTwoPlayersCantMerge() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1, 2);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(TablePausedForBalancingEvent.class, event.get().getClass());
    }

    @Test
    public void testTwoTablesOneAndThreePlayersCantMerge() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1, 3);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(TablePausedForBalancingEvent.class, event.get().getClass());
    }

    @Test
    public void testTwoTablesImbalancedByTwoCantMergeSubjectIsSmallerTableNoPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 5, 7);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(TablePausedForBalancingEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((TablePausedForBalancingEvent) event.get()).getTableId());
    }

    @Test
    public void testTwoTablesImbalancedByTwoCantMergeSubjectIsLargerTableNoPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 7, 5);
        var otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
        assertEquals(otherTableId, ((PlayerMovedToNewTableEvent) event.get()).getToTableId());
    }

    @Test
    public void testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsSmallestTableNoPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 7, 8, 9);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));

        assertEquals(TablePausedForBalancingEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((TablePausedForBalancingEvent) event.get()).getTableId());
    }

    @Test
    public void testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsLargestTableNoPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 9, 8, 7);
        var smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 7).findFirst().get()
                .getKey();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));

        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
        assertEquals(smallestOtherTableId, ((PlayerMovedToNewTableEvent) event.get()).getToTableId());
    }

}
