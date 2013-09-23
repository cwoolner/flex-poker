package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

public interface GameBso {

    Game fetchGame(Game game);

    List<Game> fetchAllGames();

    void createGame(Principal user, Game game);

    void changeGameStage(UUID gameId, GameStage starting);

    Table fetchPlayersCurrentTable(User user, Game game);

    List<Table> fetchTables(Game game);

    Set<UserGameStatus> fetchUserGameStatuses(Game game);
    
    void joinGame(UUID gameId, Principal user);

}
