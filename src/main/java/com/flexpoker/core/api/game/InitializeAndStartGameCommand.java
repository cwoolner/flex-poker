package com.flexpoker.core.api.game;

import com.flexpoker.model.Game;

public interface InitializeAndStartGameCommand {

    void execute(Game game);

}
