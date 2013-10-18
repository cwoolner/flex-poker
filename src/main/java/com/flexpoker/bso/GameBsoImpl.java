package com.flexpoker.bso;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.core.api.game.CreateGameCommand;
import com.flexpoker.core.api.game.JoinGameCommand;
import com.flexpoker.core.api.tablebalancer.AssignInitialTablesForNewGame;
import com.flexpoker.dto.CreateGameDto;
import com.flexpoker.model.Game;
import com.flexpoker.repository.api.GameRepository;

@Service
public class GameBsoImpl implements GameBso {

    private final GameRepository gameDao;

    private final JoinGameCommand joinGameCommand;

    private final CreateGameCommand createGameCommand;

    @Autowired
    public GameBsoImpl(GameRepository gameDao, JoinGameCommand joinGameCommand,
            AssignInitialTablesForNewGame assignInitialTablesForNewGame,
            CreateGameCommand createGameCommand) {
        this.gameDao = gameDao;
        this.joinGameCommand = joinGameCommand;
        this.createGameCommand = createGameCommand;
    }

    @Override
    public List<Game> fetchAllGames() {
        return gameDao.findAll();
    }

    @Override
    public void createGame(Principal principal, CreateGameDto gameDto) {
        createGameCommand.execute(principal, gameDto);
    }

    @Override
    public void joinGame(UUID gameId, Principal user) {
        joinGameCommand.execute(gameId, user);
    }

}
