package com.flexpoker.controller;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.flexpoker.bso.api.GameBso;
import com.flexpoker.bso.api.PlayerActionsBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

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
@Controller
public class FlexController {

    @Inject
    private GameBso gameBso;

    @Inject
    private EventManager eventManager;

    @Inject
    private PlayerActionsBso playerActionsBso;

    public void verifyRegistrationForGame(Game game) {
        User user = extractCurrentUser();
        eventManager.sendGameInProgressEvent(game);
    }

    public void verifyReadyToStartNewHand(Game game, Table table) {
        User user = extractCurrentUser();
        game = gameBso.fetchGame(game);
        if (GameStage.FINISHED.equals(game.getGameStage())) {
            eventManager.sendGameIsFinishedEvent(game);
        } else {
            eventManager.sendNewHandStartingEvent(game, table);
        }
    }

    public void check(Game game, Table table) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.check(game, table, user);
        eventManager.sendCheckEvent(game, table, handState, user.getUsername());
    }

    public void fold(Game game, Table table) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.fold(game, table, user);
        eventManager.sendFoldEvent(game, table, handState, user.getUsername());
    }

    public void call(Game game, Table table) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.call(game, table, user);
        eventManager.sendCallEvent(game, table, handState, user.getUsername());
    }

    public void raise(Game game, Table table, String raiseAmount) {
        User user = extractCurrentUser();
        HandState handState = playerActionsBso.raise(game, table, user, raiseAmount);
        eventManager.sendRaiseEvent(game, table, handState, user.getUsername());
    }

    private User extractCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
