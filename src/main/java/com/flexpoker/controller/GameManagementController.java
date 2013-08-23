package com.flexpoker.controller;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.ReplyToUser;
import org.springframework.messaging.simp.annotation.SubscribeEvent;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.bso.api.UserBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.model.CreateGameViewModel;
import com.flexpoker.web.translator.CreateGameTranslator;
import com.flexpoker.web.translator.GameListTranslator;

@Controller
public class GameManagementController {

    private final GameBso gameBso;
    
    private final UserBso userBso;

    @Inject
    public GameManagementController(GameBso gameBso, UserBso userBso) {
        this.gameBso = gameBso;
        this.userBso = userBso;
    }

    @SubscribeEvent(value = "/app/availabletournaments")
    public List<AvailableTournamentListViewModel> displayAllGames() {
        List<Game> gameList = gameBso.fetchAllGames();
        return new GameListTranslator().translate(gameList);
    }

    @SubscribeEvent(value = "/app/opengamesforuser")
    public List<OpenGameForUser> displayOpenGames(Principal principal) {
        return userBso.fetchUsersOpenGames(principal);
    }

    @MessageMapping(value = "/app/creategame")
    public void createGame(CreateGameViewModel model, Principal principal) {
        Game game = new CreateGameTranslator().translate(model);
        gameBso.createGame(principal, game);
    }
    
    @MessageMapping(value = "/app/joingame")
    public void joinGame(Integer gameId, Principal principal) {
        gameBso.joinGame(gameId, principal);
    }

    @MessageExceptionHandler
    @ReplyToUser(value = "/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
