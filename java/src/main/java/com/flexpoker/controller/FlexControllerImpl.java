package com.flexpoker.controller;

import java.util.List;
import java.util.Set;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.DealCardActionsBso;
import com.flexpoker.bso.GameBso;
import com.flexpoker.bso.GameEventBso;
import com.flexpoker.bso.PlayerActionsBso;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

/**
 * This implements the main and only interface used to communicate between the
 * Flex/Flash client application and the Java back-end.
 *
 * This class will grow very large due to the large number of methods, which is
 * why each method must be as short as possible.
 *
 * Certain guidelines to be observed:
 *
 * 1. This class handles fetching the current user from the SecurityContext.
 *
 * 2. This class handles coordinating when events should be sent to the
 *    subscribed clients, but should not actually handle sending the events.
 *
 * 3. The BSOs know more about when certain chunks of code need to be
 *    thread-safe.  No 'synchronized' blocks should exist in this class.
 *
 * 4. As little logic as possible should be performed in each method.  Depending
 *    on the method, each method should attempt to only contain three
 *    lines/chunks of calls.  Most, if not all, methods should be no longer than
 *    three lines.
 *
 *        a) Fetch the user.
 *        b) Call some business logic.
 *        c) Send the appropriate event.
 *
 * @author cwoolner
 */
@Controller("flexController")
@RemotingDestination
public class FlexControllerImpl implements FlexController {

    private GameBso gameBso;

    private GameEventBso gameEventBso;

    private EventManager eventManager;

    private DealCardActionsBso dealCardActionsBso;

    private PlayerActionsBso playerActionsBso;

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
        boolean gameAtUserMax = gameEventBso.addUserToGame(user, game);
        eventManager.sendUserJoinedEvent(game, user.getUsername(), gameAtUserMax);
    }
    
    @Override
    public Set<UserGameStatus> fetchAllUserGameStatuses(Game game) {
        return gameBso.fetchUserGameStatuses(game);
    }

    @Override
    public void verifyRegistrationForGame(Game game) {
        User user = extractCurrentUser();
        if (gameEventBso.verifyRegistration(user, game)) {
            eventManager.sendGameInProgressEvent(game);
        }
    }

    @Override
    public void verifyGameInProgress(Game game) {
        User user = extractCurrentUser();
        if (gameEventBso.verifyGameInProgress(user, game)) {
            eventManager.sendNewHandStartingEventForAllTables(game,
                    gameBso.fetchTables(game));
        }
    }

    @Override
    public void verifyReadyToStartNewHand(Game game, Table table) {
        User user = extractCurrentUser();
        if (gameEventBso.verifyReadyToStartNewHand(user, game, table)) {
            game = gameBso.fetchGame(game);
            if (GameStage.FINISHED.equals(game.getGameStage())) {
                eventManager.sendGameIsFinishedEvent(game);
            } else {
                eventManager.sendNewHandStartingEvent(game, table);
            }
        }
    }

    @Override
    public Table fetchPlayersCurrentTable(Game game) {
        User user = extractCurrentUser();
        return gameBso.fetchPlayersCurrentTable(user, game);
    }

    @Override
    public Table fetchTable(Game game, Table table) {
        return gameBso.fetchTable(game, table);
    }

    @Override
    public PocketCards fetchPocketCards(Game game, Table table) {
        User user = extractCurrentUser();
        return dealCardActionsBso.fetchPocketCards(user, game, table);
    }

    @Override
    public void check(Game game, Table table) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.check(game, table, user);
        eventManager.sendCheckEvent(game, table, handState, user.getUsername());
    }

    @Override
    public void fold(Game game, Table table) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.fold(game, table, user);
        eventManager.sendFoldEvent(game, table, handState, user.getUsername());
    }

    @Override
    public void call(Game game, Table table) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.call(game, table, user);
        eventManager.sendCallEvent(game, table, handState, user.getUsername());
    }

    @Override
    public void raise(Game game, Table table, String raiseAmount) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.raise(game, table, user, raiseAmount);
        eventManager.sendRaiseEvent(game, table, handState, user.getUsername());
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

    private User extractCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    public PlayerActionsBso getPlayerActionsBso() {
        return playerActionsBso;
    }

    public void setPlayerActionsBso(PlayerActionsBso playerActionsBso) {
        this.playerActionsBso = playerActionsBso;
    }

}
