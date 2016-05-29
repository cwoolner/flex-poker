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
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancerPausedTableTest {

    @Test
    public void testTwoTablesOnePausedDueToWaitingForMerge() {
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
    public void testTwoTablesOnePausedDueToNotEnoughPlayersAtTable() {
        UUID subjectTableId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableToPlayersMap = createTableToPlayersMap(
                subjectTableId, 1, 3);

        TableBalancer tableBalancer = new TableBalancer(UUID.randomUUID(), 3);
        Optional<GameEvent> event = tableBalancer.createSingleBalancingEvent(1,
                subjectTableId, Collections.emptySet(), tableToPlayersMap);
        assertEquals(TablePausedForBalancingEvent.class,
                event.get().getClass());
    }

    /*
     * @Test public void testImbalanceByTwoUnderMaxThreshold() { game =
     * GameGenerator.createGame(9, 9); Table table1 = new Table(); Table table2
     * = new Table(); DataUtilsForTests.fillTableWithUsers(table1, 3, 9);
     * DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
     * game.addTable(table1); game.addTable(table2);
     * assertFalse(command.execute(game)); }
     * 
     * @Test public void testShouldOnlyBeTwoTablesNotThree() { game =
     * GameGenerator.createGame(18, 9); Table table1 = new Table(); Table table2
     * = new Table(); Table table3 = new Table();
     * DataUtilsForTests.fillTableWithUsers(table1, 4, 9);
     * DataUtilsForTests.fillTableWithUsers(table2, 5, 9);
     * DataUtilsForTests.fillTableWithUsers(table3, 4, 9);
     * game.addTable(table1); game.addTable(table2); game.addTable(table3);
     * assertFalse(command.execute(game)); }
     * 
     * @Test public void testThreeTablesTwoOutOfBalance() { game =
     * GameGenerator.createGame(27, 9); Table table1 = new Table(); Table table2
     * = new Table(); Table table3 = new Table();
     * DataUtilsForTests.fillTableWithUsers(table1, 7, 9);
     * DataUtilsForTests.fillTableWithUsers(table2, 9, 9);
     * DataUtilsForTests.fillTableWithUsers(table3, 8, 9);
     * game.addTable(table1); game.addTable(table2); game.addTable(table3);
     * assertFalse(command.execute(game)); }
     * 
     * 
     * @Test public void testShouldBeExactlyTwoTablesNotThree() { game =
     * GameGenerator.createGame(27, 9); Table table1 = new Table(); Table table2
     * = new Table(); Table table3 = new Table();
     * DataUtilsForTests.fillTableWithUsers(table1, 6, 9);
     * DataUtilsForTests.fillTableWithUsers(table2, 6, 9);
     * DataUtilsForTests.fillTableWithUsers(table3, 6, 9);
     * game.addTable(table1); game.addTable(table2); game.addTable(table3);
     * assertFalse(command.execute(game)); }
     * 
     */
}
