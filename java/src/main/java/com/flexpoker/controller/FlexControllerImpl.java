package com.flexpoker.controller;

import java.util.List;
import java.util.Set;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.GameBso;
import com.flexpoker.bso.GameEventBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

@Controller("flexController")
@RemotingDestination
public class FlexControllerImpl implements FlexController {

    private GameBso gameBso;

    private GameEventBso gameEventBso;

    private EventManager eventManager;

    @Override
    public void createGame(Game game) {
        User user = extractCurrentUser();
        // the id is set to 0 when it comes from Flex, explicitly set it to null
        game.setId(null);
        gameBso.createGame(user, game);
        eventManager.sendGamesUpdatedEvent();
    }

    @Override
    public List<Game> fetchAllGames() {
        return gameBso.fetchAllGames();
    }

    @Override
    public void joinGame(Game game) {
        User user = extractCurrentUser();

        synchronized (this) {
            gameEventBso.addUserToGame(user, game);
            eventManager.sendUserJoinedEvent(game);
            eventManager.sendChatEvent("System", user.getUsername()
                    + " joined Game " + game.getId() + ".");

            if (gameEventBso.isGameAtMaxPlayers(game)) {
                gameBso.createRealTimeGame(game);
                gameBso.changeGameStage(game, GameStage.STARTING);
                eventManager.sendGamesUpdatedEvent();
                eventManager.sendGameStartingEvent(game);
            }
        }

    }
    
    @Override
    public Set<UserGameStatus> fetchAllUserGameStatuses(Game game) {
        game = gameBso.fetchById(game.getId());
        return game.getUserGameStatuses();
    }

    @Override
    public void verifyRegistrationForGame(Game game) {
        User user = extractCurrentUser();

        synchronized (this) {
            gameEventBso.verifyRegistration(user, game);

            if (gameEventBso.haveAllPlayersVerifiedRegistration(game)) {
                gameBso.intializePlayersAndTables(game);
                gameBso.changeGameStage(game, GameStage.IN_PROGRESS);
                eventManager.sendGameInProgressEvent(game);
            }
        }
    }

    @Override
    public void verifyGameInProgress(Game game) {
        User user = extractCurrentUser();

        synchronized (this) {
            gameEventBso.verifyGameInProgress(user, game);

            if (gameEventBso.haveAllPlayersVerifiedGameInProgress(game)) {
                gameEventBso.startNewHandForAllTables(game);
                sendNewHandStartingEventForAllTables(game);
            }
        }
    }

    @Override
    public Table fetchTable(Game game) {
        User user = extractCurrentUser();
        return gameBso.fetchPlayersCurrentTable(user, game);
    }

    @Override
    public PocketCards fetchPocketCards(Table table) {
        User user = extractCurrentUser();
        return gameEventBso.fetchPocketCards(user, table);
    }

    @Override
    public void check(Table table) {
        User user = extractCurrentUser();

        synchronized (this) {
            if (gameEventBso.isUserAllowedToPerformAction(GameEventType.CHECK,
                    user, table)) {
                gameEventBso.check(user, table);
                gameEventBso.updateState(table);
                eventManager.sendChatEvent("System", user.getUsername()
                    + " checks.");
                determineNextEvent(table);
            }
        }
    }

    private void determineNextEvent(Table table) {
        if (gameEventBso.isRoundComplete(table)) {
            if (!gameEventBso.isFlopDealt(table)) {
                eventManager.sendDealFlopEvent(table);
            } else if (!gameEventBso.isTurnDealt(table)) {
                eventManager.sendDealTurnEvent(table);
            } else if (!gameEventBso.isRiverDealt(table)) {
                eventManager.sendDealRiverEvent(table);
            } else if (gameEventBso.isHandComplete(table)) {
                eventManager.sendHandCompleteEvent(table);
            } else {
                throw new IllegalStateException("The game is not in the "
                        + "correct state.  One of the above round complete "
                        + "events should have been sent.");
            }
        } else {
            // TODO: Implement
        }
    }

    private User extractCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void sendNewHandStartingEventForAllTables(Game game) {
        game = gameBso.fetchById(game.getId());

        for (Table table : game.getTables()) {
            eventManager.sendNewHandStartingEvent(table);
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
