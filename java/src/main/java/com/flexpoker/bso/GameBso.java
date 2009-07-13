package com.flexpoker.bso;

import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.model.GameType;

public interface GameBso {

    List<Game> fetchAllGames();

    void createGame(Game game);

    List<GameType> fetchAllGameTypes();

}
