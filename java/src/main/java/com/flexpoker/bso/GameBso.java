package com.flexpoker.bso;

import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameBso {

    List<Game> fetchAllGames();

    void createGame(Game game);

    Game fetchById(Integer id);

    void changeGameStage(Game game, String gameStageName);

    Table fetchPlayersCurrentTable(User user, Game game);

}
