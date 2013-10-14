package com.flexpoker.core.api.game;

import java.security.Principal;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisherAware;

public interface JoinGameCommand extends ApplicationEventPublisherAware {

    void execute(UUID gameId, Principal principal);

}
