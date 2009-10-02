package com.flexpoker.bso.mock;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.TableBalancerBso;
import com.flexpoker.model.Table;
import com.flexpoker.model.TableMovement;
import com.flexpoker.model.UserGameStatus;

@Service
public class TableBalancerBsoMock implements TableBalancerBso {

    @Override
    public boolean areTablesBalanced(List<Table> tables) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Table> assignInitialTablesForNewGame(Set<UserGameStatus> userGameStatuses, int maxPlayersPerTableForGame) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TableMovement> calculateTableMovements(List<Table> tables) {
        // TODO Auto-generated method stub
        return null;
    }

}
