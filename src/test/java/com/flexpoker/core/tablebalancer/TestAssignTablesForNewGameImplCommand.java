package com.flexpoker.core.tablebalancer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.util.DataUtilsForTests;

public class TestAssignTablesForNewGameImplCommand {

    @Mock private ValidationBso mockValidationBso;
    
    @Mock private GameRepository mockGameRepository;
    
    private AssignTablesForNewGameImplCommand command;

    private UUID gameId;
    
    private Game game;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameId = UUID.randomUUID();
        game = new Game();
        when(mockGameRepository.findById(gameId)).thenReturn(game);
        command = new AssignTablesForNewGameImplCommand(mockValidationBso, mockGameRepository);
    }
    
    @Test
    public void testFourFitsAllInOneTable() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(4);
        game.setMaxPlayersPerTable(9);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 9, 4);
    }

    @Test
    public void testFourFitsAllInASmallerTable() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(4);
        game.setMaxPlayersPerTable(6);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 6, 4);
    }

    @Test
    public void testFourFitsPerfectlyInOneTable() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(4);
        game.setMaxPlayersPerTable(4);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 4, 4);
    }

    @Test
    public void testFourFitsUnevenlyInTwoTables() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(4);
        game.setMaxPlayersPerTable(3);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(2, tables.size());
        verifyEqualDistribution(tables, 3, 2, 2);
    }

    @Test
    public void testFourFitsPerfectlyInTwoTables() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(4);
        game.setMaxPlayersPerTable(2);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(2, tables.size());
        verifyEqualDistribution(tables, 2, 2, 2);
    }

    @Test
    public void testTwentyFitsUnevenlyOverThreeTables() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(20);
        game.setMaxPlayersPerTable(9);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(3, tables.size());
        verifyEqualDistribution(tables, 9, 7, 7, 6);
    }

    @Test
    public void testTwentyFitsFourEvenTablesNoneFull() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(20);
        game.setMaxPlayersPerTable(6);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(4, tables.size());
        verifyEqualDistribution(tables, 6, 5, 5, 5, 5);
    }

    @Test
    public void testTwentyFitsFiveEvenTablesAllFull() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(20);
        game.setMaxPlayersPerTable(4);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(5, tables.size());
        verifyEqualDistribution(tables, 4, 4, 4, 4, 4, 4);
    }

    @Test
    public void testTwentyFitsSevenTablesUnevenly() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(20);
        game.setMaxPlayersPerTable(3);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(7, tables.size());
        verifyEqualDistribution(tables, 3, 3, 3, 3, 3, 3, 3, 2);
    }

    @Test
    public void testTwentyFitsTenTablesEvenly() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(20);
        game.setMaxPlayersPerTable(2);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(10, tables.size());
        verifyEqualDistribution(tables, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
    }

    @Test
    public void testTwoFitsOneTable1() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(2);
        game.setMaxPlayersPerTable(9);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 9, 2);
    }

    @Test
    public void testTwoFitsOneTable2() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(2);
        game.setMaxPlayersPerTable(6);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 6, 2);
    }

    @Test
    public void testTwoFitsOneTable3() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(2);
        game.setMaxPlayersPerTable(4);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 4, 2);
    }

    @Test
    public void testTwoFitsOneTable4() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(2);
        game.setMaxPlayersPerTable(3);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 3, 2);
    }

    @Test
    public void testTwoFitsOneTable5() {
        Set<UserGameStatus> userGameStatuses = DataUtilsForTests.createUserGameStatusSet(2);
        game.setMaxPlayersPerTable(2);
        addUserGameStatusesToRealTimeGame(userGameStatuses);
        command.execute(gameId);
        List<Table> tables = game.getTables();
        assertEquals(1, tables.size());
        verifyEqualDistribution(tables, 2, 2);
    }

    private void verifyEqualDistribution(List<Table> tables,
            int maxPlayersPerTableForGame, int... expectedUserGameStatusesPerTable) {

        for (Table table : tables) {
            assertEquals(maxPlayersPerTableForGame, table.getSeats().size());
        }

        int[] actualUserGameStatusesPerTable = new int[tables.size()];

        int i = 0;
        for (Table table : tables) {
            int numberOfUserGameStatuses = 0;
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null) {
                    numberOfUserGameStatuses++;
                }
            }
            actualUserGameStatusesPerTable[i] = numberOfUserGameStatuses;
            i++;
        }

        Arrays.sort(expectedUserGameStatusesPerTable);
        Arrays.sort(actualUserGameStatusesPerTable);

        assertArrayEquals(expectedUserGameStatusesPerTable, actualUserGameStatusesPerTable);
    }

    private void addUserGameStatusesToRealTimeGame(Set<UserGameStatus> userGameStatuses) {
        for (UserGameStatus userGameStatus : userGameStatuses) {
            game.addUserGameStatus(userGameStatus);
        }
    }
    
}
