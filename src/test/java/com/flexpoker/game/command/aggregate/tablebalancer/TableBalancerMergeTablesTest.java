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
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancerMergeTablesTest {

    @Test
    public void testTwoTablesOneAlreadyPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 1);
        UUID otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.singleton(otherTableId),
                tableToPlayersMap);
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
    }

    @Test
    public void testTwoTablesReadyToMerge() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
    }

    @Test
    public void testThreeTablesReadyToMerge() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 3, 2);
        UUID smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 2).findFirst().get()
                .getKey();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
        assertEquals(smallestOtherTableId,
                ((PlayerMovedToNewTableEvent) event.get()).getToTableId());
    }

}
