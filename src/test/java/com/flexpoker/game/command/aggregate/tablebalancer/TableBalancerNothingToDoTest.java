package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancerNothingToDoTest {

    @Test
    public void testSingleTableTwoPlayers() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testTwoTablesTwoPlayersEach() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testTwoTablesOneAlreadyPausedWaitingForMerge() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2, 1);
        UUID otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.singleton(otherTableId),
                tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testTwoTablesWithinOneOfEachOtherAndUnderMergeThreshold() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 6, 7);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testThreeTablesPerfectlyInBalanceAndUnderMergeThreshold() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 7, 7, 7);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testThreeTablesAllAtMax() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 6, 6, 6);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 6);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testThreeTablesTwoImbalancedByTwoCantMergeSubjectIsMediumTableSmallestPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 8, 7, 9);
        UUID smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 7).findFirst().get()
                .getKey();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.singleton(smallestOtherTableId),
                tableToPlayersMap);
        assertFalse(event.isPresent());
    }

    @Test
    public void testThreeTablesAllImbalancedCantMergeSubjectIsMediumTableSmallestPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 7, 6, 9);
        UUID smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 6).findFirst().get()
                .getKey();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.singleton(smallestOtherTableId),
                tableToPlayersMap);
        assertFalse(event.isPresent());
    }

}
