package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.GameEventBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

@Service
public class GameEventBsoMock implements GameEventBso {

    @Override
    public boolean addUserToGame(User user, Game game) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean verifyGameInProgress(User user, Game game) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean verifyRegistration(User user, Game game) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean verifyReadyToStartNewHand(User user, Game game, Table table) {
        // TODO Auto-generated method stub
        return false;
    }

}
