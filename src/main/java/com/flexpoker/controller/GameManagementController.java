package com.flexpoker.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flexpoker.bso.GameBso;
import com.flexpoker.model.Game;

@Controller
@RequestMapping(value = "/gamemanagement")
public class GameManagementController {

    private final GameBso gameBso;
    
    @Inject
    public GameManagementController(GameBso gameBso) {
        this.gameBso = gameBso;
    }
    
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String displayAllGames(Model model) {
        List<Game> allGames = gameBso.fetchAllGames();
        model.addAttribute("allGames", allGames);
        return "gamelist";
    }
    
}
