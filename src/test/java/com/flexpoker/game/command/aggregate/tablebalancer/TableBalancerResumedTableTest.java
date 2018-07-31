package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.game.command.aggregate.TableBalancer;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;

public class TableBalancerResumedTableTest {

    @Test
    void testOneBalancedTableThatIsPaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 2);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.singleton(subjectTableId),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(TableResumedAfterBalancingEvent.class, event.get().getClass());
        assertEquals(subjectTableId, ((TableResumedAfterBalancingEvent) event.get()).getTableId());
    }

    @Test
    void testTwoBalancedTablesOnePaused() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 8, 8);
        var otherTableId = tableToPlayersMap.keySet().stream()
                .filter(x -> !x.equals(subjectTableId)).findFirst().get();

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 9);
        var event = tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.singleton(otherTableId),
                tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap));
        assertEquals(TableResumedAfterBalancingEvent.class, event.get().getClass());
        assertEquals(otherTableId, ((TableResumedAfterBalancingEvent) event.get()).getTableId());
    }

}
