package com.flexpoker.bso;

import java.util.List;
import java.util.Set;

import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

public interface GameBso {

    List<Game> fetchAllGames();

    void createGame(User user, Game game);

    Game fetchById(Integer id);

    void changeGameStage(Game game, GameStage starting);

    Table fetchPlayersCurrentTable(User user, Game game);

    void initializePlayersAndTables(Game game);

    List<Table> fetchTables(Game game);

    Set<UserGameStatus> fetchUserGameStatuses(Game game);

}
