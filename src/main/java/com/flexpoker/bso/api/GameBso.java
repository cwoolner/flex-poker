package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

public interface GameBso {

    Game fetchGame(Game game);

    List<Game> fetchAllGames();

    void createGame(Principal user, Game game);

    void changeGameStage(Integer gameId, GameStage starting);

    Table fetchTable(Game game, Table table);

    Table fetchPlayersCurrentTable(User user, Game game);

    void initializePlayersAndTables(Game game);

    List<Table> fetchTables(Game game);

    Set<UserGameStatus> fetchUserGameStatuses(Game game);
    
    void joinGame(Integer gameId, Principal user);

}
