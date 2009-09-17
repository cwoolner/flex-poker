package com.flexpoker.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.model.Game;
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
            eventEventManager.sendUserJoinedEvent(game);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        eventEventManager.sendUserJoinedEvent(game);
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
        fail("Not yet implemented");
    }

    @Test
    public void testSendDealFlopEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testSendDealRiverEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testSendDealTurnEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testSendHandCompleteEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testSendUserActedEvent() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetMessageTemplate() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetMessageTemplate() {
        fail("Not yet implemented");
    }

}
