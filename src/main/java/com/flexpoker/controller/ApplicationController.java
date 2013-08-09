package com.flexpoker.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.messaging.simp.annotation.SubscribeEvent;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.model.Game;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.translator.GameListTranslator;

@Controller
public class ApplicationController {

    private final GameBso gameBso;

    @Inject
    public ApplicationController(GameBso gameBso) {
        this.gameBso = gameBso;
    }

    @SubscribeEvent(value = "/app/availabletournaments")
    public List<AvailableTournamentListViewModel> displayAllGames() {
        List<Game> gameList = gameBso.fetchAllGames();
        return new GameListTranslator().translate(gameList);
    }

}
