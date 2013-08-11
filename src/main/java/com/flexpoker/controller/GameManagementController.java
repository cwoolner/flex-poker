package com.flexpoker.controller;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeEvent;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.model.Game;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.model.CreateGameViewModel;
import com.flexpoker.web.translator.CreateGameTranslator;
import com.flexpoker.web.translator.GameListTranslator;

@Controller
public class GameManagementController {

    private final GameBso gameBso;
    
    @Inject
    public GameManagementController(GameBso gameBso) {
        this.gameBso = gameBso;
    }
    
    @SubscribeEvent(value = "/app/availabletournaments")
    public List<AvailableTournamentListViewModel> displayAllGames() {
        List<Game> gameList = gameBso.fetchAllGames();
        return new GameListTranslator().translate(gameList);
    }

    @MessageMapping(value="/app/creategame")
    public void createGame(CreateGameViewModel model, Principal principal) {
        Game game = new CreateGameTranslator().translate(model);
        gameBso.createGame(principal, game);
    }
}
