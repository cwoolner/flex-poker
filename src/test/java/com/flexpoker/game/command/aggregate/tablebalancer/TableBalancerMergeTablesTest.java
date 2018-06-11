package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;

public class TableBalancerMergeTablesTest {

    @Test
    public void testTwoTablesOneAlreadyPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1, 1);
        var otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.singleton(otherTableId),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
    }

    @Test
    public void testTwoTablesReadyToMerge() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1, 2);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
    }

    @Test
    public void testThreeTablesReadyToMerge() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1, 3, 2);
        var smallestOtherTableId = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().size() == 2).findFirst().get()
                .getKey();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(PlayerMovedToNewTableEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((PlayerMovedToNewTableEvent) event.get()).getFromTableId());
        assertEquals(smallestOtherTableId, ((PlayerMovedToNewTableEvent) event.get()).getToTableId());
    }

}
