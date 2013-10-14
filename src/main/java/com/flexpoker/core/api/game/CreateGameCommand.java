package com.flexpoker.core.api.game;

import java.security.Principal;

import com.flexpoker.model.Game;

public interface CreateGameCommand {

    void execute(Principal principal, Game game);
    
}
