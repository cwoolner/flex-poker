package com.flexpoker.controller;

import java.util.List;

import com.flexpoker.model.Game;

public interface FlexController {

    List<Game> fetchAllGames();

    void createGame(Game game);

    void joinGame(Game game);

}
