package com.flexpoker.web.controller;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.commands.GameCommand;
import com.flexpoker.game.command.commands.JoinGameCommand;
import com.flexpoker.game.query.dto.GameInListDTO;
import com.flexpoker.game.query.dto.OpenGameForUser;
import com.flexpoker.game.query.repository.GameListRepository;
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository;
import com.flexpoker.login.repository.LoginRepository;
import com.flexpoker.web.dto.incoming.CreateGameDTO;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class GameManagementController {

    private final OpenGameForPlayerRepository openGameForUserRepository;

    private final CommandSender<GameCommand> commandSender;

    private final LoginRepository loginRepository;

    private final GameListRepository gameRepository;

    @Inject
    public GameManagementController(
            OpenGameForPlayerRepository openGameForUserRepository,
            CommandSender<GameCommand> commandSender,
            LoginRepository loginRepository, GameListRepository gameRepository) {
        this.openGameForUserRepository = openGameForUserRepository;
        this.commandSender = commandSender;
        this.loginRepository = loginRepository;
        this.gameRepository = gameRepository;
    }

    @SubscribeMapping("/topic/availabletournaments")
    public List<GameInListDTO> displayAllGames() {
        return gameRepository.fetchAll();
    }

    @SubscribeMapping("/app/opengamesforuser")
    public List<OpenGameForUser> displayOpenGames(Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        return openGameForUserRepository.fetchAllOpenGamesForPlayer(playerId);
    }

    @MessageMapping("/app/creategame")
    public void createGame(CreateGameDTO model, Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        var command = new CreateGameCommand(model.getName(), model.getPlayers(), model.getPlayersPerTable(), playerId,
                model.getNumberOfMinutesBetweenBlindLevels(), model.getNumberOfSecondsForActionOnTimer());
        commandSender.send(command);
    }

    @MessageMapping("/app/joingame")
    public void joinGame(UUID gameId, Principal principal) {
        var playerId = loginRepository.fetchAggregateIdByUsername(principal.getName());
        var command = new JoinGameCommand(gameId, playerId);
        commandSender.send(command);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
