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
import com.flexpoker.game.command.framework.GameEvent;

public class TableBalancer {

    private final int maxPlayersPerTable;

    private final UUID gameId;

    public TableBalancer(UUID gameId, int maxPlayersPerTable) {
        this.maxPlayersPerTable = maxPlayersPerTable;
        this.gameId = gameId;
    }

    public Optional<GameEvent> createSingleBalancingEvent(int version,
            UUID subjectTableId, Set<UUID> pausedTablesForBalancing,
            Map<UUID, Set<UUID>> tableToPlayersMap) {

        // make sure at least two players exists. we're in a bad state if not
        int totalNumberOfPlayers = getTotalNumberOfPlayers(tableToPlayersMap);
        if (totalNumberOfPlayers < 2) {
            throw new FlexPokerException(
                    "the game should be over since less than two players are left.  no balancing should be done");
        }

        // remove any empty tables first
        Optional<Entry<UUID, Set<UUID>>> emptyTable = tableToPlayersMap
                .entrySet().stream().filter(x -> x.getValue().isEmpty())
                .findFirst();
        if (emptyTable.isPresent()) {
            return Optional.of(new TableRemovedEvent(gameId, version,
                    emptyTable.get().getKey()));
        }

        // check to see if the number of tables is greater than the number it
        // should have. if so, move all the players from this table to other
        // tables starting with the one with the lowest number
        if (tableToPlayersMap
                .size() > getRequiredNumberOfTables(totalNumberOfPlayers)) {
            UUID playerToMove = tableToPlayersMap.get(subjectTableId).stream()
                    .findFirst().get();
            UUID toTableId = tableToPlayersMap.entrySet().stream()
                    .filter(x -> !x.getKey().equals(subjectTableId))
                    .min(setSizeComparator()).get().getKey();
            return Optional.of(new PlayerMovedToNewTableEvent(gameId, version,
                    subjectTableId, toTableId, playerToMove));
        }

        if (tableToPlayersMap.get(subjectTableId).size() == 1) {
            return Optional.of(new TablePausedForBalancingEvent(gameId, version,
                    subjectTableId));
        }

        return Optional.empty();
    }

    private Comparator<? super Entry<UUID, Set<UUID>>> setSizeComparator() {
        return (x, y) -> Integer.compare(x.getValue().size(),
                y.getValue().size());
    }

    private int getTotalNumberOfPlayers(
            Map<UUID, Set<UUID>> tableToPlayersMap) {
        return tableToPlayersMap.values().stream()
                .collect(Collectors.summingInt(Set::size));
    }

    private int getRequiredNumberOfTables(int totalNumberOfPlayers) {
        int numberOfRequiredTables = totalNumberOfPlayers / maxPlayersPerTable;

        if (totalNumberOfPlayers % maxPlayersPerTable != 0) {
            numberOfRequiredTables++;
        }

        return numberOfRequiredTables;
    }

    private boolean arePlayersDistributedEvenly(
            Map<UUID, Integer> tableSizesMap) {
        int minSize = -1;
        int maxSize = -1;

        for (Integer tableSize : tableSizesMap.values()) {
            if (minSize == -1) {
                minSize = tableSize;
            }
            if (maxSize == -1) {
                maxSize = tableSize;
            }

            if (tableSize < minSize) {
                minSize = tableSize;
            }
            if (tableSize > maxSize) {
                maxSize = tableSize;
            }
        }

        return minSize + 2 > maxSize;
    }

}
