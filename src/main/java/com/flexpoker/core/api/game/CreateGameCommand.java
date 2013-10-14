package com.flexpoker.core.api.game;

import java.security.Principal;

import com.flexpoker.dto.CreateGameDto;

public interface CreateGameCommand {

    void execute(Principal principal, CreateGameDto game);
    
}
