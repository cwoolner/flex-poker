package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.TableBalancerBso;
import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.TableMovement;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.util.Constants;

@Service
public class TableBalancerBsoImpl implements TableBalancerBso {

    private ValidationBso validationBso;

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
    
    @Override
    public List<Table> assignInitialTablesForNewGame(Set<UserGameStatus> userGameStatuses,
            int maxPlayersPerTable) {
        validationBso.validateTableAssignment(userGameStatuses, maxPlayersPerTable);
        int numberOfTables = calculateNumberOfTables(userGameStatuses,
                maxPlayersPerTable);
        List<Table> tables = populateSeats(maxPlayersPerTable, numberOfTables);
        List<UserGameStatus> randomOrderedUserGameStatusList =
                randomizeUserGameStatuses(userGameStatuses);
        distributeUserGameStatusesToTables(randomOrderedUserGameStatusList, tables);
        return tables;
    }

    private void distributeUserGameStatusesToTables(List<UserGameStatus> userGameStatuses,
            List<Table> tables) {
        for (int i = 0; i < userGameStatuses.size(); ) {
            for (Table table : tables) {
                if (i < userGameStatuses.size()) {
                    addUserGameStatusToAnyEmptySeat(table, userGameStatuses.get(i));
                    i++;
                }
            }
        }
    }

    private void addUserGameStatusToAnyEmptySeat(Table table, UserGameStatus userGameStatus) {
        synchronized (table) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() == null) {
                    seat.setUserGameStatus(userGameStatus);
                    return;
                }
            }

            throw new IllegalArgumentException("No empty seats were found.");
        }
    }

    private List<UserGameStatus> randomizeUserGameStatuses(Set<UserGameStatus> userGameStatuses) {
        List<UserGameStatus> userGameStatusList =
                new ArrayList<UserGameStatus>(userGameStatuses);
        Collections.shuffle(userGameStatusList, new Random());
        return userGameStatusList;
    }

    private List<Table> populateSeats(int maxPlayersPerTable, int numberOfTables) {
        List<Table> tables = new ArrayList<Table>();

        for (int i = 0; i < numberOfTables; i++) {
            Table table = new Table();

            List<Seat> seats = new ArrayList<Seat>();
            for (int j = 0; j < maxPlayersPerTable; j++) {
                Seat seat = new Seat();
                seat.setPosition(j);
                seats.add(seat);
            }

            table.setSeats(seats);
            tables.add(table);
        }
        return tables;
    }

    private int calculateNumberOfTables(Set<UserGameStatus> userGameStatuses, int maxPlayersPerTable) {
        int numberOfTables = userGameStatuses.size() / maxPlayersPerTable;

        // if the number of people doesn't fit perfectly, then an additional
        // table is needed for the overflow
        if (userGameStatuses.size() % maxPlayersPerTable != 0) {
            numberOfTables++;
        }
        return numberOfTables;
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

    public ValidationBso getValidationBso() {
        return validationBso;
    }

    public void setValidationBso(ValidationBso validationBso) {
        this.validationBso = validationBso;
    }

}
