package com.flexpoker.bso.mock;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.GameEventBso;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service("gameEventBsoMock")
public class GameEventBsoMock implements GameEventBso {

    @Override
    public void addUserToGame(User user, Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PocketCards fetchPocketCards(User user, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean haveAllPlayersVerifiedGameInProgress(Game game) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean haveAllPlayersVerifiedRegistration(Game game) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFlopDealt(Table table) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGameAtMaxPlayers(Game game) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHandComplete(Table table) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRiverDealt(Table table) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRoundComplete(Table table) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTurnDealt(Table table) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUserAllowedToPerformAction(GameEventType action, User user, Table table) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setRoundComplete(Table table, boolean b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void startNewHand(Game game, Table table) {
        // TODO Auto-generated method stub

    }

    @Override
    public void startNewHandForAllTables(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateCheckState(Table table) {
        // TODO Auto-generated method stub

    }

    @Override
    public void verifyGameInProgress(User user, Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void verifyRegistration(User user, Game game) {
        // TODO Auto-generated method stub

    }

}
