package com.flexpoker.core.game;

import java.security.Principal;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.CreateGameCommand;
import com.flexpoker.event.GameListUpdatedEvent;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.User;
import com.flexpoker.repository.api.GameRepository;
import com.flexpoker.repository.api.UserRepository;

@Command
public class CreateGameImplCommand implements CreateGameCommand {

    private final UserRepository userRepository;
    
    private final GameRepository gameRepository;
    
    private ApplicationEventPublisher eventPublisher;
    
    @Inject
    public CreateGameImplCommand(UserRepository userRepository,
            GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }
    
    @Override
    public void execute(Principal principal, Game game) {
        User user = userRepository.findByUsername(principal.getName());

        game.setCreatedByUser(user);
        game.setCreatedOn(new Date());
        game.setGameStage(GameStage.REGISTERING);
        game.setAllowRebuys(false);
        gameRepository.saveNew(game);
        
        eventPublisher.publishEvent(new GameListUpdatedEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

}
