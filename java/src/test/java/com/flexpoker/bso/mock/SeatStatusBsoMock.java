package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.SeatStatusBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

@Service
public class SeatStatusBsoMock implements SeatStatusBso {

    @Override
    public void setStatusForNewGame(Game game, Table table) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStatusForNewRound(Game game, Table table) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStatusForNewHand(Game game, Table table) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStatusForEndOfHand(Game game, Table table) {}

}
