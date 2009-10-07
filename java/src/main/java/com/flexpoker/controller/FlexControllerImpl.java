package com.flexpoker.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.DealCardActionsBso;
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

    private DealCardActionsBso dealCardActionsBso;

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
                gameBso.changeGameStage(game, GameStage.STARTING);
                eventManager.sendGamesUpdatedEvent();
                eventManager.sendGameStartingEvent(game);
            }
        }

    }
    
    @Override
    public Set<UserGameStatus> fetchAllUserGameStatuses(Game game) {
        return gameBso.fetchUserGameStatuses(game);
    }

    @Override
    public void verifyRegistrationForGame(Game game) {
        User user = extractCurrentUser();

        synchronized (this) {
            gameEventBso.verifyRegistration(user, game);

            if (gameEventBso.haveAllPlayersVerifiedRegistration(game)) {
                gameBso.initializePlayersAndTables(game);
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
    public PocketCards fetchPocketCards(Game game, Table table) {
        User user = extractCurrentUser();
        return dealCardActionsBso.fetchPocketCards(user, table);
    }

    @Override
    public void check(Game game, Table table) {
        User user = extractCurrentUser();
        game = gameBso.fetchById(game.getId());

        synchronized (this) {
            if (gameEventBso.isUserAllowedToPerformAction(GameEventType.CHECK,
                    user, table)) {
                gameEventBso.updateCheckState(table);
                eventManager.sendChatEvent("System", user.getUsername()
                    + " checks.");
                determineNextEvent(game, table);
            }
        }
    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        return dealCardActionsBso.fetchFlopCards(game, table);
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        return dealCardActionsBso.fetchRiverCard(game, table);
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        return dealCardActionsBso.fetchTurnCard(game, table);
    }

    @Override
    public Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table) {
        //TODO: This shoud have an additional "can they do this? check.
        return gameEventBso.fetchOptionalShowCards(game, table);
    }

    @Override
    public Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table) {
        // TODO: This should have an additional "can they do this? check.
        return gameEventBso.fetchRequiredShowCards(game, table);
    }

    private void determineNextEvent(Game game, Table table) {
        if (gameEventBso.isRoundComplete(table)) {
            gameEventBso.setRoundComplete(table, false);

            if (!gameEventBso.isFlopDealt(table)) {
                eventManager.sendDealFlopEvent(game, table);
            } else if (!gameEventBso.isTurnDealt(table)) {
                eventManager.sendDealTurnEvent(game, table);
            } else if (!gameEventBso.isRiverDealt(table)) {
                eventManager.sendDealRiverEvent(game, table);
            } else if (gameEventBso.isHandComplete(table)) {
                eventManager.sendHandCompleteEvent(game, table);
            } else {
                throw new IllegalStateException("The game is not in the "
                        + "correct state.  One of the above round complete "
                        + "events should have been sent.");
            }
        } else {
            eventManager.sendUserActedEvent(game, table);
        }
    }

    private User extractCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void sendNewHandStartingEventForAllTables(Game game) {
        game = gameBso.fetchById(game.getId());

        List<Table> tables = gameBso.fetchTables(game);

        for (Table table : tables) {
            eventManager.sendNewHandStartingEvent(game, table);
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

    public DealCardActionsBso getDealCardActionsBso() {
        return dealCardActionsBso;
    }

    public void setDealCardActionsBso(DealCardActionsBso dealCardActionsBso) {
        this.dealCardActionsBso = dealCardActionsBso;
    }

}
