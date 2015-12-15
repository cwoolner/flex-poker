package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class TableAssignmentTest {

    @Test
    public void testAssignmentIsRandom() {
        boolean player1AlwaysWithPlayer2 = true;
        boolean player1SometimesWithPlayer2 = false;

        for (int i = 0; i < 1000; i++) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreatedEvent(UUID.randomUUID(), 1, "test", 4, 2,
                    UUID.randomUUID()));

            Game game = new DefaultGameFactory().createFrom(events);

            // create a bunch of static UUIDs that will hash the same and will
            // thus be transformed into the same list before shuffling occurs
            // (and will fail the test without the shuffle().
            // this gets rid of the randomizing effect of using a HashSet with
            // random/on-the-fly UUIDs, which isn't good when remain attached to
            //  players over periods of time
            UUID player1Id = UUID.fromString("07755923-95b4-4ae7-9f45-b67a8e7929fe");
            UUID player2Id = UUID.fromString("07755923-95b4-4ae7-9f45-b67a8e7929ff");
            UUID player3Id = UUID.fromString("17755923-95b4-4ae7-9f45-b67a8e7929fe");
            UUID player4Id = UUID.fromString("17755923-95b4-4ae7-9f45-b67a8e7929ff");

            game.joinGame(player1Id);
            game.joinGame(player2Id);
            game.joinGame(player3Id);
            game.joinGame(player4Id);

            GameTablesCreatedAndPlayersAssociatedEvent event = (GameTablesCreatedAndPlayersAssociatedEvent) game
                    .fetchAppliedEvents().get(6);

            Map<UUID, Set<UUID>> tableIdToPlayerIdsMap = event
                    .getTableIdToPlayerIdsMap();

            Set<UUID> player1sTable = tableIdToPlayerIdsMap.values().stream()
                    .filter(y -> y.contains(player1Id)).findAny().get();

            if (player1sTable.contains(player2Id)) {
                player1SometimesWithPlayer2 = true;
            } else {
                player1AlwaysWithPlayer2 = false;
            }
        }

        assertFalse(player1AlwaysWithPlayer2);
        assertTrue(player1SometimesWithPlayer2);
    }

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

        Game game = new DefaultGameFactory().createFrom(events);

        Stream.iterate(1, e -> e + 1)
                .limit(numberOfPlayers)
                .forEach(x -> game.joinGame(UUID.randomUUID()));

        return game;
    }

    private void verifyTableDistribution(Game game, int... expectedPlayersPerTable) {
        int expectedNumberOfTables = expectedPlayersPerTable.length;
        int totalNumberOfEvents = game.fetchNewEvents().size();

        // this event will change locations depending on the number of players,
        // but it should be the 2nd to last every time
        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchNewEvents().get(totalNumberOfEvents - 2);
        Map<UUID, Set<UUID>> tableIdToPlayerIdsMap = gameTablesCreatedAndPlayersAssociatedEvent
                .getTableIdToPlayerIdsMap();

        List<Integer> playerSizes = tableIdToPlayerIdsMap.values().stream()
                .map(x -> x.size())
                .sorted()
                .collect(Collectors.toList());
        List<Integer> playersPerTableList = IntStream.of(expectedPlayersPerTable)
                .boxed()
                .collect(Collectors.toList());

        assertEquals(expectedNumberOfTables, tableIdToPlayerIdsMap.size());
        assertEquals(playerSizes, playersPerTableList);
    }

}
