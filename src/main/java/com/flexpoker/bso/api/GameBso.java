package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import com.flexpoker.dto.CreateGameDto;
import com.flexpoker.model.Game;

public interface GameBso {

    List<Game> fetchAllGames();

    void createGame(Principal user, CreateGameDto createGameDto);

    void joinGame(UUID gameId, Principal user);

}
