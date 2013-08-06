package com.flexpoker.bso.api;

import java.util.List;
import java.util.Set;

import com.flexpoker.model.Table;
import com.flexpoker.model.TableMovement;
import com.flexpoker.model.UserGameStatus;

public interface TableBalancerBso {

    boolean areTablesBalanced(List<Table> tables);

    Set<TableMovement> calculateTableMovements(List<Table> tables);

    List<Table> assignInitialTablesForNewGame(Set<UserGameStatus> userGameStatuses,
            int maxPlayersPerTableForGame);

}
