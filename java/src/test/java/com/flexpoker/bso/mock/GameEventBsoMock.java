package com.flexpoker.bso.mock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.GameEventBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
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
    public HandState check(Game game, Table table, User user) {
        // TODO Auto-generated method stub
        return null;
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
    public boolean verifyReadyToStartNewHand(User user, Game game, Table table) {
        // TODO Auto-generated method stub
        return false;
    }

}
