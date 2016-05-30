package com.flexpoker.game.command.aggregate.tablebalancer;

import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createDefaultChipMapForSubjectTable;
import static com.flexpoker.game.command.aggregate.tablebalancer.TableBalancerTestUtils.createTableToPlayersMap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.aggregate.TableBalancer;

public class TableBalancerExceptionTest {

    @Test(expected = FlexPokerException.class)
    public void testSingleTableOnePlayer() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        tableBalancer.createSingleBalancingEvent(1, subjectTableId,
                Collections.emptySet(), tableToPlayersMap,
                createDefaultChipMapForSubjectTable(subjectTableId,
                        tableToPlayersMap));
    }

    @Test(expected = FlexPokerException.class)
    public void testTwoTablesOnePlayer() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 0);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        tableBalancer.createSingleBalancingEvent(1, subjectTableId,
                Collections.emptySet(), tableToPlayersMap,
                createDefaultChipMapForSubjectTable(subjectTableId,
                        tableToPlayersMap));
    }

    @Test(expected = FlexPokerException.class)
    public void testAllTablesEmpty() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 0, 0);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 2);
        tableBalancer.createSingleBalancingEvent(1, subjectTableId,
                Collections.emptySet(), tableToPlayersMap,
                createDefaultChipMapForSubjectTable(subjectTableId,
                        tableToPlayersMap));
    }

}
