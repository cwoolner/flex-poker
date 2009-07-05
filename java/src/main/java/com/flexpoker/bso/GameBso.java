package com.flexpoker.bso;

import java.util.List;

import com.flexpoker.model.Game;

public interface GameBso {

    List<Game> fetchAllGames();

    void createGame();

}
