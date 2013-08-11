package com.flexpoker.web.translator;

import com.flexpoker.model.Game;
import com.flexpoker.web.model.CreateGameViewModel;

public class CreateGameTranslator {

    public Game translate(CreateGameViewModel viewModel) {
        Game game = new Game();
        game.setTotalPlayers(viewModel.getPlayers());
        game.setMaxPlayersPerTable(viewModel.getPlayersPerTable());
        return game;
    }

}
