package com.flexpoker.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flexpoker.model.Game;
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
    public void testSendDealFlopEvent() {
        Game game = new Game();
        Table table = new Table();
        
        try {
            eventEventManager.sendDealFlopEvent(game, table);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        table.setId(1);
        eventEventManager.sendDealFlopEvent(game, table);
    }

    @Test
    public void testSendDealRiverEvent() {
        Game game = new Game();
        Table table = new Table();
        
        try {
            eventEventManager.sendDealRiverEvent(game, table);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        table.setId(1);
        eventEventManager.sendDealRiverEvent(game, table);
    }

    @Test
    public void testSendDealTurnEvent() {
        Game game = new Game();
        Table table = new Table();
        
        try {
            eventEventManager.sendDealTurnEvent(game, table);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        table.setId(1);
        eventEventManager.sendDealTurnEvent(game, table);
    }

    @Test
    public void testSendHandCompleteEvent() {
        Game game = new Game();
        Table table = new Table();
        
        try {
            eventEventManager.sendHandCompleteEvent(game, table);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        table.setId(1);
        eventEventManager.sendHandCompleteEvent(game, table);
    }

    @Test
    public void testSendUserActedEvent() {
        Game game = new Game();
        Table table = new Table();
        
        try {
            eventEventManager.sendUserActedEvent(game, table);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {}

        game.setId(1);
        table.setId(1);
        eventEventManager.sendUserActedEvent(game, table);
    }

}
