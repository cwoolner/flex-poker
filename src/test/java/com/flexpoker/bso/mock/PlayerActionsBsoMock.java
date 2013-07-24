package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.PlayerActionsBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

@Service
public class PlayerActionsBsoMock implements PlayerActionsBso {

    @Override
    public HandState check(Game game, Table table, User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandState fold(Game game, Table table, User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandState call(Game game, Table table, User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandState raise(Game game, Table table, User user, String raiseAmount) {
        // TODO Auto-generated method stub
        return null;
    }

}
