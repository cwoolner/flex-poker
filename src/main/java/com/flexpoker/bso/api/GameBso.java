package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import com.flexpoker.model.Game;

public interface GameBso {

    Game fetchGame(Game game);

    List<Game> fetchAllGames();

    void createGame(Principal user, Game game);

    void joinGame(UUID gameId, Principal user);

}
