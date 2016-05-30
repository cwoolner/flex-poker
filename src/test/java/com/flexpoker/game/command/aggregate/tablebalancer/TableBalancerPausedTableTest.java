package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancerPausedTableTest {

    @Test
    public void testTwoTablesOneAndTwoPlayersCantMerge() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TablePausedForBalancingEvent.class,
                event.get().getClass());
    }

    @Test
    public void testTwoTablesOneAndThreePlayersCantMerge() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 3);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TablePausedForBalancingEvent.class,
                event.get().getClass());
    }

    @Test
    public void testTwoTablesImbalancedByTwoCantMergeSubjectIsSmallerTableNoPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 5, 7);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TablePausedForBalancingEvent.class,
                event.get().getClass());
        assertEquals(subjectTableId,
                ((TablePausedForBalancingEvent) event.get()).getTableId());
    }

    @Test
    public void testTwoTablesImbalancedByTwoCantMergeSubjectIsLargerTableNoPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 7, 5);
        UUID otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
        assertEquals(otherTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getToTableId());
    }

    @Test
    public void testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsSmallestTableNoPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 7, 8, 9);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);

        assertEquals(TablePausedForBalancingEvent.class,
                event.get().getClass());
        assertEquals(subjectTableId,
                ((TablePausedForBalancingEvent) event.get()).getTableId());
    }

    @Test
    public void testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsLargestTableNoPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 9, 8, 7);
        UUID smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 7).findFirst().get()
                .getKey();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);

        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
        assertEquals(smallestOtherTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getToTableId());
    }

}
