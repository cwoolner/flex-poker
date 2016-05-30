package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancerResumedTableTest {

    @Test
    public void testOneBalancedTableThatIsPaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 2);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.singleton(subjectTableId),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(
                        subjectTableId, tableToPlayersMap));
        assertEquals(TableResumedAfterBalancingEvent.class,
                event.get().getClass());
        assertEquals(subjectTableId,
                ((TableResumedAfterBalancingEvent) event.get()).getTableId());
    }

    @Test
    public void testTwoBalancedTablesOnePaused() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 8, 8);
        UUID otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.singleton(otherTableId),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(
                        subjectTableId, tableToPlayersMap));
        assertEquals(TableResumedAfterBalancingEvent.class,
                event.get().getClass());
        assertEquals(otherTableId,
                ((TableResumedAfterBalancingEvent) event.get()).getTableId());
    }

}
