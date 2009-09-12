package com.flexpoker.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.GameBso;
import com.flexpoker.bso.GameEventBso;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
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
        // TODO: This should have an additional "can they do this?" check.
        User user = extractCurrentUser();
        return gameEventBso.fetchPocketCards(user, table);
    }

    @Override
    public void check(Table table) {
        User user = extractCurrentUser();

        synchronized (this) {
            if (gameEventBso.isUserAllowedToPerformAction(GameEventType.CHECK,
                    user, table)) {
                gameEventBso.updateCheckState(table);
                eventManager.sendChatEvent("System", user.getUsername()
                    + " checks.");
                determineNextEvent(table);
            }
        }
    }

    @Override
    public FlopCards fetchFlopCards(Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return gameEventBso.fetchFlopCards(table);
    }

    @Override
    public RiverCard fetchRiverCard(Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return gameEventBso.fetchRiverCard(table);
    }

    @Override
    public TurnCard fetchTurnCard(Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return gameEventBso.fetchTurnCard(table);
    }

    @Override
    public Map<Integer, PocketCards> fetchOptionalShowCards(Table table) {
        //TODO: This shoud have an additional "can they do this? check.
        return gameEventBso.fetchOptionalShowCards(table);
    }

    @Override
    public Map<Integer, PocketCards> fetchRequiredShowCards(Table table) {
        // TODO: This should have an additional "can they do this? check.
        return gameEventBso.fetchRequiredShowCards(table);
    }

    private void determineNextEvent(Table table) {
        if (gameEventBso.isRoundComplete(table)) {
            gameEventBso.setRoundComplete(table, false);

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
            eventManager.sendUserActedEvent(table);
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
