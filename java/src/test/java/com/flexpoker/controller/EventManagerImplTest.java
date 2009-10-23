package com.flexpoker.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;
import com.flexpoker.util.Context;


public class EventManagerImplTest {

    private EventManagerImpl eventEventManager = (EventManagerImpl)
            Context.instance().getBean("eventManager");

    @Test
    public void testSendGamesUpdatedEvent() {
        eventEventManager.sendGamesUpdatedEvent();
    }

    @Test
    public void testSendUserJoinedEvent() {
        Game game = new Game();

        try {
            eventEventManager.sendUserJoinedEvent(game, "john", false);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        eventEventManager.sendUserJoinedEvent(game, "john", false);
        eventEventManager.sendUserJoinedEvent(game, "john", true);
    }

    @Test
    public void testSendChatEvent() {
        eventEventManager.sendChatEvent("jgalt", "it shrugged");

        try {
            eventEventManager.sendChatEvent("", "it shrugged");
            fail ("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {};

        try {
            eventEventManager.sendChatEvent("jgalt", "");
            fail ("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {};

        try {
            eventEventManager.sendChatEvent("", "");
            fail ("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {};

    }

    @Test
    public void testSendGameStartingEvent() {
        Game game = new Game();

        try {
            eventEventManager.sendGameStartingEvent(game);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        eventEventManager.sendGameStartingEvent(game);
    }

    @Test
    public void testSendGameInProgressEvent() {
        Game game = new Game();

        try {
            eventEventManager.sendGameInProgressEvent(game);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        eventEventManager.sendGameInProgressEvent(game);
    }

    @Test
    public void testSendNewHandStartingEvent() {
        Game game = new Game();
        Table table = new Table();
        
        try {
            eventEventManager.sendNewHandStartingEvent(game, table);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        table.setId(1);
        eventEventManager.sendNewHandStartingEvent(game, table);
    }

    @Test
    public void testSendCheckEvent() {
        // first test should fail due to no ids set on the table or game
        Game game = new Game();
        Table table = new Table();
        HandState handState = new HandState(HandDealerState.RIVER_DEALT,
                HandRoundState.ROUND_COMPLETE);
        try {
            eventEventManager.sendCheckEvent(game, table, handState, "galt");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        // set the ids so this test should pass
        game.setId(1);
        table.setId(1);
        eventEventManager.sendCheckEvent(game, table, handState, "galt");

        testIllegalStateGameEvent(HandDealerState.NONE, HandRoundState.ROUND_IN_PROGRESS);
        testValidGameEvent(HandDealerState.POCKET_CARDS_DEALT, HandRoundState.ROUND_IN_PROGRESS);
        testValidGameEvent(HandDealerState.FLOP_DEALT, HandRoundState.ROUND_IN_PROGRESS);
        testValidGameEvent(HandDealerState.TURN_DEALT, HandRoundState.ROUND_IN_PROGRESS);
        testValidGameEvent(HandDealerState.RIVER_DEALT, HandRoundState.ROUND_IN_PROGRESS);
        testIllegalStateGameEvent(HandDealerState.COMPLETE, HandRoundState.ROUND_IN_PROGRESS);
        testIllegalStateGameEvent(HandDealerState.NONE, HandRoundState.ROUND_COMPLETE);
        testIllegalStateGameEvent(HandDealerState.POCKET_CARDS_DEALT, HandRoundState.ROUND_COMPLETE);
        testValidGameEvent(HandDealerState.FLOP_DEALT, HandRoundState.ROUND_COMPLETE);
        testValidGameEvent(HandDealerState.TURN_DEALT, HandRoundState.ROUND_COMPLETE);
        testValidGameEvent(HandDealerState.RIVER_DEALT, HandRoundState.ROUND_COMPLETE);
        testValidGameEvent(HandDealerState.COMPLETE, HandRoundState.ROUND_COMPLETE);
    }

    private void testIllegalStateGameEvent(HandDealerState handDealerState,
            HandRoundState handRoundState) {
        Game game = new Game();
        game.setId(1);
        Table table = new Table();
        table.setId(1);
        HandState handState = new HandState(handDealerState, handRoundState);
        try {
            eventEventManager.sendCheckEvent(game, table, handState, "galt");
            fail("Should have thrown IllegalStateException.");
        } catch (IllegalStateException e) {}
    }

    private void testValidGameEvent(HandDealerState handDealerState,
            HandRoundState handRoundState) {
        Game game = new Game();
        game.setId(1);
        Table table = new Table();
        table.setId(1);
        HandState handState = new HandState(handDealerState, handRoundState);
        eventEventManager.sendCheckEvent(game, table, handState, "galt");
    }

}
