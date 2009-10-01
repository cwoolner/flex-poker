package com.flexpoker.bso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TableMovement;
import com.flexpoker.util.Constants;

@Service("tableBalancerBso")
public class TableBalancerBsoImpl implements TableBalancerBso {

    @Override
    public boolean areTablesBalanced(List<Table> tables) {
        Map<Integer, Integer> tableSizesMap = findTableSizes(tables);

        if (!isNumberOfTablesCorrect(tables, tableSizesMap)) {
            return false;
        }

        return arePlayersDistributedEvenly(tableSizesMap);
    }

    @Override
    public Set<TableMovement> calculateTableMovements(List<Table> tables) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private boolean isNumberOfTablesCorrect(List<Table> tables, Map<Integer, Integer> tableSizesMap) {
        int totalNumberOfPlayers = 0;
        
        for (Integer tableSize : tableSizesMap.values()) {
            totalNumberOfPlayers += tableSize;
        }

        int numberOfRequiredTables = totalNumberOfPlayers / Constants.MAX_PLAYERS_PER_TABLE;

        if (totalNumberOfPlayers % Constants.MAX_PLAYERS_PER_TABLE != 0) {
            numberOfRequiredTables++;
        }

        return numberOfRequiredTables == tables.size();
    }

    private boolean arePlayersDistributedEvenly(Map<Integer, Integer> tableSizesMap) {
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

    private Map<Integer, Integer> findTableSizes(List<Table> tables) {
        Map<Integer, Integer> tableSizesMap = new HashMap<Integer, Integer>();

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
