package com.flexpoker.core.api.game;

import java.security.Principal;

import org.springframework.context.ApplicationEventPublisherAware;

import com.flexpoker.model.Game;

public interface CreateGameCommand extends ApplicationEventPublisherAware {

    void execute(Principal principal, Game game);
    
}
