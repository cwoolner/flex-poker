package com.flexpoker.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.model.Game;
import com.flexpoker.web.model.AvailableTournamentListViewModel;
import com.flexpoker.web.translator.GameListTranslator;

@Controller
@RequestMapping(value = "/gamemanagement")
public class GameManagementController {

    private final GameBso gameBso;
    
    @Inject
    public GameManagementController(GameBso gameBso) {
        this.gameBso = gameBso;
    }
    
    @RequestMapping(value = "/tournament/registering/list", method = RequestMethod.GET)
    public @ResponseBody List<AvailableTournamentListViewModel> displayAllGames(Model model) {
        List<Game> gameList = gameBso.fetchAllGames();
        return new GameListTranslator().translate(gameList);
    }
    
}
