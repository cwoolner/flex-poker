package com.flexpoker.core.tablebalancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.flexpoker.config.Query;
import com.flexpoker.core.api.tablebalancer.AreTablesBalancedQuery;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.Constants;

@Query
public class AreTablesBalancedImplQuery implements AreTablesBalancedQuery {

    private final RealTimeGameRepository realTimeGameRepository;
    
    @Inject
    public AreTablesBalancedImplQuery(RealTimeGameRepository realTimeGameRepository) {
        this.realTimeGameRepository = realTimeGameRepository;
    }
    
    @Override
    public boolean execute(Integer gameId) {
        RealTimeGame realTimeGame = realTimeGameRepository.get(gameId);
        List<Table> tables = realTimeGame.getTables();
        
        Map<Integer, Integer> tableSizesMap = findTableSizes(tables);

        if (!isNumberOfTablesCorrect(tables, tableSizesMap)) {
            return false;
        }

        return arePlayersDistributedEvenly(tableSizesMap);
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
