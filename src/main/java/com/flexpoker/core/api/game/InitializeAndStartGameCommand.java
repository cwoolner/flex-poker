package com.flexpoker.core.api.game;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisherAware;

public interface InitializeAndStartGameCommand extends
        ApplicationEventPublisherAware {

    void execute(UUID gameId);

}
