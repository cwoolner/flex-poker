package com.flexpoker.bso.mock;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.GameBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

@Service
public class GameBsoMock implements GameBso {

    @Override
    public void changeGameStage(Game game, GameStage starting) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createGame(User user, Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Game> fetchAllGames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Table fetchPlayersCurrentTable(User user, Game game) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Table> fetchTables(Game game) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<UserGameStatus> fetchUserGameStatuses(Game game) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void initializePlayersAndTables(Game game) {
        // TODO Auto-generated method stub

    }

}
