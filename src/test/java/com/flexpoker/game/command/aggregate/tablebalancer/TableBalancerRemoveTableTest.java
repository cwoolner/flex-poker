package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.events.TableRemovedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancerRemoveTableTest {

    @Test
    public void testSubjectTableIsEmpty() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 0, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TableRemovedEvent.class, event.get().getClass());
        assertEquals(subjectTableId,
                ((TableRemovedEvent) event.get()).getTableId());
    }

    @Test
    public void testSingleOtherEmptyTable() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2, 0);
        UUID otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TableRemovedEvent.class, event.get().getClass());
        assertEquals(otherTableId,
                ((TableRemovedEvent) event.get()).getTableId());
    }

    @Test
    public void testMultipleOtherEmptyTables() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2, 0, 0);
        Set<UUID> otherTableIds = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId))
                .collect(Collectors.toSet());

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TableRemovedEvent.class, event.get().getClass());
        assertTrue(otherTableIds
                .contains(((TableRemovedEvent) event.get()).getTableId()));
    }

    @Test
    public void testNoEmptyOtherwiseBalancedTables() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertFalse(event.isPresent());
    }

}
