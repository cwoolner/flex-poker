package com.flexpoker.bso.mock;

import java.util.Map;

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
    public Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startNewHand(Game game, Table table) {
        // TODO Auto-generated method stub

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

}
