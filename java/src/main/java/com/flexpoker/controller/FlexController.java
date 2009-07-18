package com.flexpoker.controller;

import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.model.UserStatusInGame;

public interface FlexController {

    List<Game> fetchAllGames();

    void createGame(Game game);

    void joinGame(Game game);

    List<UserStatusInGame> fetchAllUserStatusesForGame(Game game);

}
