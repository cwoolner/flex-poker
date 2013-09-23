package com.flexpoker.core.tablebalancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.config.Query;
import com.flexpoker.core.api.tablebalancer.AreTablesBalancedQuery;
import com.flexpoker.model.Game;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.Constants;

@Query
public class AreTablesBalancedImplQuery implements AreTablesBalancedQuery {

    private final RealTimeGameRepository realTimeGameRepository;
    
    private final GameRepository gameRepository;
    
    @Inject
    public AreTablesBalancedImplQuery(
            RealTimeGameRepository realTimeGameRepository,
            GameRepository gameRepository) {
        this.realTimeGameRepository = realTimeGameRepository;
        this.gameRepository = gameRepository;
    }
    
    @Override
    public boolean execute(UUID gameId) {
        Game game = gameRepository.findById(gameId);
        RealTimeGame realTimeGame = realTimeGameRepository.get(gameId);
        List<Table> tables = realTimeGame.getTables();
        
        Map<UUID, Integer> tableSizesMap = findTableSizes(tables);

        if (!isNumberOfTablesCorrect(tables, tableSizesMap, game.getMaxPlayersPerTable())) {
            return false;
        }

        return arePlayersDistributedEvenly(tableSizesMap);
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
