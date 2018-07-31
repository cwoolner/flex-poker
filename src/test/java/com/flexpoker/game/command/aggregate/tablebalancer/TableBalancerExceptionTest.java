package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.aggregate.TableBalancer;

public class TableBalancerExceptionTest {

    @Test
    void testSingleTableOnePlayer() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        assertThrows(FlexPokerException.class,
                () -> tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                        tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap)));
    }

    @Test
    void testTwoTablesOnePlayer() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 1, 0);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        assertThrows(FlexPokerException.class,
                () -> tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                        tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap)));
    }

    @Test
    void testAllTablesEmpty() {
        var subjectTableId = UUID.randomUUID();
        var tableToPlayersMap = createTableToPlayersMap(subjectTableId, 0, 0);

        var tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        assertThrows(FlexPokerException.class,
                () -> tableBalancer.createSingleBalancingEvent(subjectTableId, Collections.emptySet(),
                        tableToPlayersMap, createDefaultChipMapForSubjectTable(subjectTableId, tableToPlayersMap)));
    }

}
