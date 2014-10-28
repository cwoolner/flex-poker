package com.flexpoker.web.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.commands.JoinGameCommand;
import com.flexpoker.game.command.framework.GameCommandType;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.web.model.CreateGameViewModel;
import com.flexpoker.web.model.GameInListDTO;

@Controller
public class GameManagementController {

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final CommandPublisher<GameCommandType> commandPublisher;

    private final LoginRepository loginRepository;

    private final GameListRepository gameRepository;

    @Inject
    public GameManagementController(OpenGameForPlayerRepository openGameForUserRepository,
            CommandPublisher<GameCommandType> commandPublisher,
            LoginRepository loginRepository, GameListRepository gameRepository) {
        this.openGameForUserRepository = openGameForUserRepository;
        this.commandPublisher = commandPublisher;
        this.loginRepository = loginRepository;
        this.gameRepository = gameRepository;
    }

    @SubscribeMapping(value = "/topic/availabletournaments")
    public List<GameInListDTO> displayAllGames() {
        return gameRepository.fetchAll();
    }

    @SubscribeMapping(value = "/app/opengamesforuser")
    public List<OpenGameForUser> displayOpenGames(Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        return openGameForUserRepository.fetchAllOpenGamesForPlayer(playerId);
    }

    @MessageMapping(value = "/app/creategame")
    public void createGame(CreateGameViewModel model, Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        CreateGameCommand command = new CreateGameCommand(model.getName(),
                model.getPlayers(), model.getPlayersPerTable(), playerId);
        commandPublisher.publish(command);
    }

    @MessageMapping(value = "/app/joingame")
    public void joinGame(String gameId, Principal principal) {
        UUID playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        UUID gameAggregateId = UUID.fromString(gameId);
        JoinGameCommand command = new JoinGameCommand(gameAggregateId, playerId);
        commandPublisher.publish(command);
    }

    @MessageExceptionHandler
    @SendToUser(value = "/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
