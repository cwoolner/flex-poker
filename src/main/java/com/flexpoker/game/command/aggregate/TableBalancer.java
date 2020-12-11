package com.flexpoker.game.command.aggregate;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.events.TableRemovedEvent;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.game.command.events.GameEvent;

public class TableBalancer {

    private final int maxPlayersPerTable;

    private final UUID gameId;

    public TableBalancer(UUID gameId, int maxPlayersPerTable) {
        this.maxPlayersPerTable = maxPlayersPerTable;
        this.gameId = gameId;
    }

    public Optional<GameEvent> createSingleBalancingEvent(UUID subjectTableId, Set<UUID> pausedTablesForBalancing,
            Map<UUID, Set<UUID>> tableToPlayersMap, Map<UUID, Integer> playerToChipsAtTableMap) {

        // make sure at least two players exists. we're in a bad state if not
        var totalNumberOfPlayers = getTotalNumberOfPlayers(tableToPlayersMap);
        if (totalNumberOfPlayers < 2) {
            throw new FlexPokerException(
                    "the game should be over since less than two players are left.  no balancing should be done");
        }

        // remove any empty tables first
        var emptyTable = tableToPlayersMap.entrySet().stream()
                .filter(x -> x.getValue().isEmpty())
                .findFirst();
        if (emptyTable.isPresent()) {
            return Optional.of(new TableRemovedEvent(gameId, emptyTable.get().getKey()));
        }

        // check to see if the number of tables is greater than the number it
        // should have. if so, move all the players from this table to other
        // tables starting with the one with the lowest number
        if (tableToPlayersMap.size() > getRequiredNumberOfTables(totalNumberOfPlayers)) {
            return createEventToMoveUserFromSubjectTableToAnyMinTable(subjectTableId, tableToPlayersMap,
                    playerToChipsAtTableMap);
        }

        if (isTableOutOfBalance(subjectTableId, tableToPlayersMap)) {
            var tableSize = tableToPlayersMap.get(subjectTableId).size();

            // in the min case, pause the table since it needs to get another
            // player
            if (tableSize == tableToPlayersMap.values().stream()
                    .map(x -> x.size()).min(Integer::compare).get()) {
                return Optional.of(new TablePausedForBalancingEvent(gameId, subjectTableId));
            }

            // only move a player if the out of balance table has the max number
            // of players. an out-of-balance medium-sized table should not send
            // any players
            if (tableSize == tableToPlayersMap.values().stream()
                    .map(x -> x.size()).max(Integer::compare).get()) {
                return createEventToMoveUserFromSubjectTableToAnyMinTable(subjectTableId, tableToPlayersMap,
                        playerToChipsAtTableMap);
            }
        }

        // re-examine the paused tables to see if they are still not balanced
        var pausedTablesThatShouldStayPaused = pausedTablesForBalancing.stream()
                .filter(x -> isTableOutOfBalance(x, tableToPlayersMap))
                .collect(Collectors.toSet());

        // resume the first table that is currently paused, but are now balanced
        var tableToResume = pausedTablesForBalancing.stream()
                .filter(x -> !pausedTablesThatShouldStayPaused.contains(x))
                .findFirst();
        if (tableToResume.isPresent()) {
            return Optional.of(new TableResumedAfterBalancingEvent(gameId, tableToResume.get()));
        }

        return Optional.empty();
    }

    private Optional<GameEvent> createEventToMoveUserFromSubjectTableToAnyMinTable(UUID subjectTableId,
            Map<UUID, Set<UUID>> tableToPlayersMap, Map<UUID, Integer> playerToChipsAtTableMap) {
        var playerToMove = tableToPlayersMap.get(subjectTableId).stream()
                .findFirst().get();
        var toTableId = findNonSubjectMinTableId(subjectTableId, tableToPlayersMap);
        int chips = playerToChipsAtTableMap.get(playerToMove);
        return Optional.of(new PlayerMovedToNewTableEvent(gameId, subjectTableId, toTableId, playerToMove, chips));
    }

    private UUID findNonSubjectMinTableId(UUID subjectTableId,
            Map<UUID, Set<UUID>> tableToPlayersMap) {
        return tableToPlayersMap.entrySet().stream()
                .filter(x -> !x.getKey().equals(subjectTableId))
                .min(setSizeComparator()).get().getKey();
    }

    private boolean isTableOutOfBalance(UUID tableId,
            Map<UUID, Set<UUID>> tableToPlayersMap) {
        var tableSize = tableToPlayersMap.get(tableId).size();

        if (tableSize == 1) {
            return true;
        }

        return tableToPlayersMap.values().stream()
                .anyMatch(x -> x.size() >= tableSize + 2 || x.size() <= tableSize - 2);
    }

    private Comparator<? super Entry<UUID, Set<UUID>>> setSizeComparator() {
        return (x, y) -> Integer.compare(x.getValue().size(), y.getValue().size());
    }

    private int getTotalNumberOfPlayers(Map<UUID, Set<UUID>> tableToPlayersMap) {
        return tableToPlayersMap.values().stream()
                .collect(Collectors.summingInt(Set::size));
    }

    private int getRequiredNumberOfTables(int totalNumberOfPlayers) {
        var numberOfRequiredTables = totalNumberOfPlayers / maxPlayersPerTable;

        if (totalNumberOfPlayers % maxPlayersPerTable != 0) {
            numberOfRequiredTables++;
        }

        return numberOfRequiredTables;
    }

}
