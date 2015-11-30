package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEvent;

public class TableAssignmentTests {

    private final GameFactory gameFactory = new DefaultGameFactory();

    @Test
    public void testTwoPlayersOneTable() {
        Game game = createGameAndJoinAllPlayers(2, 2);
        verifyTableDistribution(game, 2);
    }

    @Test
    public void testFourFitsAllInOneTable() {
        Game game = createGameAndJoinAllPlayers(4, 9);
        verifyTableDistribution(game, 4);
    }

    @Test
    public void testFourFitsAllInASmallerTable() {
        Game game = createGameAndJoinAllPlayers(4, 6);
        verifyTableDistribution(game, 4);
    }

    @Test
    public void testFourFitsPerfectlyInOneTable() {
        Game game = createGameAndJoinAllPlayers(4, 4);
        verifyTableDistribution(game, 4);
    }

    @Test
    public void testFourFitsEvenlyInTwoTables() {
        Game game = createGameAndJoinAllPlayers(4, 3);
        verifyTableDistribution(game, 2, 2);
    }

    @Test
    public void testTwentyFitsUnevenlyOverThreeTables() {
        Game game = createGameAndJoinAllPlayers(20, 9);
        verifyTableDistribution(game, 6, 7, 7);
    }

    @Test
    public void testTwentyFitsFourEvenTablesNoneFull() {
        Game game = createGameAndJoinAllPlayers(20, 6);
        verifyTableDistribution(game, 5, 5, 5, 5);
    }

    @Test
    public void testTwentyFitsFiveEvenTablesAllFull() {
        Game game = createGameAndJoinAllPlayers(20, 4);
        verifyTableDistribution(game, 4, 4, 4, 4, 4);
    }

    @Test
    public void testTwentyFitsSevenTablesUnevenly() {
        Game game = createGameAndJoinAllPlayers(20, 3);
        verifyTableDistribution(game, 2, 3, 3, 3, 3, 3, 3);
    }

    @Test
    public void testTwentyFitsTenTablesEvenly() {
        Game game = createGameAndJoinAllPlayers(20, 2);
        verifyTableDistribution(game, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
    }

    @Test
    public void testTwoFitsOneTable1() {
        Game game = createGameAndJoinAllPlayers(2, 9);
        verifyTableDistribution(game, 2);
    }

    @Test
    public void testTwoFitsOneTable2() {
        Game game = createGameAndJoinAllPlayers(2, 6);
        verifyTableDistribution(game, 2);
    }

    @Test
    public void testTwoFitsOneTable3() {
        Game game = createGameAndJoinAllPlayers(2, 4);
        verifyTableDistribution(game, 2);
    }

    @Test
    public void testTwoFitsOneTable4() {
        Game game = createGameAndJoinAllPlayers(2, 3);
        verifyTableDistribution(game, 2);
    }

    @Test
    public void testTwoFitsOneTable5() {
        Game game = createGameAndJoinAllPlayers(2, 2);
        verifyTableDistribution(game, 2);
    }

    private Game createGameAndJoinAllPlayers(int numberOfPlayers,
            int numberOfPlayersPerTable) {
        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), 1, "test",
                numberOfPlayers, numberOfPlayersPerTable, UUID.randomUUID()));

        Game game = gameFactory.createFrom(events);

        for (int i = 0; i < numberOfPlayers; i++) {
            game.joinGame(UUID.randomUUID());
        }

        return game;
    }

    private void verifyTableDistribution(Game game, int... playersPerTable) {
        int numberOfTables = playersPerTable.length;
        int totalNumberOfEvents = game.fetchNewEvents().size();

        // this event will change locations depending on the number of players,
        // but it should be the 2nd to last every time
        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchNewEvents().get(totalNumberOfEvents - 2);

        int actualNumberOfTables = gameTablesCreatedAndPlayersAssociatedEvent
                .getTableIdToPlayerIdsMap().size();
        assertEquals(numberOfTables, actualNumberOfTables);

        int[] actualNumberOfPlayersAtEachTable = new int[actualNumberOfTables];

        List<Set<UUID>> playersForEachTable = new ArrayList<>(
                gameTablesCreatedAndPlayersAssociatedEvent
                        .getTableIdToPlayerIdsMap().values());

        for (int i = 0; i < actualNumberOfTables; i++) {
            actualNumberOfPlayersAtEachTable[i] = playersForEachTable.get(i)
                    .size();
        }

        Arrays.sort(actualNumberOfPlayersAtEachTable);
        assertArrayEquals(playersPerTable, actualNumberOfPlayersAtEachTable);
    }

}
