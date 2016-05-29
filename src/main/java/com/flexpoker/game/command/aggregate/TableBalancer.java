package com.flexpoker.game.command.aggregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.events.TableRemovedEvent;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

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

        if (tableToPlayersMap.get(subjectTableId).size() == 1) {
            return Optional.of(new TablePausedForBalancingEvent(gameId, version,
                    subjectTableId));
        }

        return Optional.empty();
    }

    private int getTotalNumberOfPlayers(
            Map<UUID, Set<UUID>> tableToPlayersMap) {
        return tableToPlayersMap.values().stream()
                .collect(Collectors.summingInt(Set::size));
    }

    private boolean isNumberOfTablesCorrect(List<Table> tables,
            Map<UUID, Integer> tableSizesMap, int maxPlayersPerTable) {
        int totalNumberOfPlayers = 0;

        for (Integer tableSize : tableSizesMap.values()) {
            totalNumberOfPlayers += tableSize;
        }

        int numberOfRequiredTables = totalNumberOfPlayers / maxPlayersPerTable;

        if (totalNumberOfPlayers % maxPlayersPerTable != 0) {
            numberOfRequiredTables++;
        }

        return numberOfRequiredTables == tables.size();
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

    private Map<UUID, Integer> findTableSizes(List<Table> tables) {
        Map<UUID, Integer> tableSizesMap = new HashMap<>();

        for (Table table : tables) {
            tableSizesMap.put(table.getId(), determineNumberOfPlayers(table));
        }
        return tableSizesMap;
    }

    private Integer determineNumberOfPlayers(Table table) {
        int numberOfPlayers = 0;

        for (Seat seat : table.getSeats()) {
            if (seat.getUserGameStatus() != null) {
                numberOfPlayers++;
            }
        }

        return numberOfPlayers;
    }

}
