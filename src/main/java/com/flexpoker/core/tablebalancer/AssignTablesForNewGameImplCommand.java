package com.flexpoker.core.tablebalancer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.tablebalancer.AssignInitialTablesForNewGame;
import com.flexpoker.model.Game;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.repository.api.RealTimeGameRepository;

@Command
public class AssignTablesForNewGameImplCommand implements AssignInitialTablesForNewGame {

    private final ValidationBso validationBso;

    private final RealTimeGameRepository realTimeGameRepository;
    
    private final GameRepository gameRepository;

    @Inject
    public AssignTablesForNewGameImplCommand(
            ValidationBso validationBso,
            RealTimeGameRepository realTimeGameRepository,
            GameRepository gameRepository) {
        this.validationBso = validationBso;
        this.realTimeGameRepository = realTimeGameRepository;
        this.gameRepository = gameRepository;
    }
    
    @Override
    public void execute(UUID gameId) {
        Game game = gameRepository.findById(gameId);
        RealTimeGame realTimeGame = realTimeGameRepository.get(gameId);
        
        Set<UserGameStatus> userGameStatuses = realTimeGame.getUserGameStatuses();
        int maxPlayersPerTable = game.getMaxPlayersPerTable();
        
        validationBso.validateTableAssignment(userGameStatuses, maxPlayersPerTable);
        
        int numberOfTables = calculateNumberOfTables(userGameStatuses, maxPlayersPerTable);
        List<Table> tables = populateSeats(maxPlayersPerTable, numberOfTables);
        List<UserGameStatus> randomOrderedUserGameStatusList = randomizeUserGameStatuses(userGameStatuses);
        distributeUserGameStatusesToTables(randomOrderedUserGameStatusList, tables);

        for (Table table : tables) {
            realTimeGame.addTable(table);
        }
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

    private List<UserGameStatus> randomizeUserGameStatuses(Set<UserGameStatus> userGameStatuses) {
        List<UserGameStatus> userGameStatusList =
                new ArrayList<UserGameStatus>(userGameStatuses);
        Collections.shuffle(userGameStatusList, new Random());
        return userGameStatusList;
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
    
}
