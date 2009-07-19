package com.flexpoker.controller;

import java.util.List;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.GameBso;
import com.flexpoker.bso.GameEventBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.User;
import com.flexpoker.model.UserStatusInGame;

@Controller("flexController")
@RemotingDestination
public class FlexControllerImpl implements FlexController {

    private GameBso gameBso;

    private GameEventBso gameEventBso;

    private EventManager eventManager;

    @Override
    public void createGame(Game game) {
        gameBso.createGame(game);
        eventManager.sendGamesUpdatedEvent();
    }

    @Override
    public List<Game> fetchAllGames() {
        return gameBso.fetchAllGames();
    }

    @Override
    public void joinGame(Game game) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        synchronized (this) {
            gameEventBso.addUserToGame(user, game);
            eventManager.sendUserJoinedEvent(game);
            eventManager.sendChatEvent("System", user.getUsername()
                    + " joined Game " + game.getId() + ".");

            if (gameEventBso.isGameAtMaxPlayers(game)) {
                gameBso.changeGameStage(game, GameStage.STARTING);
                eventManager.sendGamesUpdatedEvent();
                eventManager.sendGameStartingEvent(game);
            }
        }

    }
    
    @Override
    public List<UserStatusInGame> fetchAllUserStatusesForGame(Game game) {
        game = gameBso.fetchById(game.getId());
        return game.getUserStatusInGames();
    }

    @Override
    public void verifyRegistrationForGame(Game game) {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        synchronized (this) {
            gameEventBso.verifyRegistration(user, game);

            if (gameEventBso.areAllPlayerRegistrationsVerified(game)) {
                eventManager.sendGameInProgressEvent(game);
            }
        }
    }

    public GameBso getGameBso() {
        return gameBso;
    }

    public void setGameBso(GameBso gameBso) {
        this.gameBso = gameBso;
    }

    public GameEventBso getGameEventBso() {
        return gameEventBso;
    }

    public void setGameEventBso(GameEventBso gameEventBso) {
        this.gameEventBso = gameEventBso;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

}
