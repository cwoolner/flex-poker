package com.flexpoker.game.command.aggregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.table.command.framework.TableEvent;

public class TableBalancer {

    private final Map<UUID, Set<UUID>> tableIdToPlayerIdsMap;
    
    public TableBalancer(Map<UUID, Set<UUID>> tableIdToPlayerIdsMap) {
        this.tableIdToPlayerIdsMap = tableIdToPlayerIdsMap;
    }

    public List<TableEvent> balanceTables() {
        // TODO Auto-generated method stub
        return null;
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

    private boolean arePlayersDistributedEvenly(Map<UUID, Integer> tableSizesMap) {
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
