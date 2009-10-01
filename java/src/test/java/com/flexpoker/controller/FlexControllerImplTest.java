package com.flexpoker.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.context.SecurityContextHolder;

import com.flexpoker.model.Game;
import com.flexpoker.util.Context;


public class FlexControllerImplTest {

    private FlexControllerImpl flexController = (FlexControllerImpl)
            Context.instance().getBean("flexController");

    @Test
    public void testCreateGame() {
        assertTrue(0 == 0);
    }

    @Test
    public void testFetchAllGames() {
        assertTrue(0 == 0);
    }

    @Test
    public void testJoinGame() {
        assertTrue(0 == 0);
    }

    @Test
    public void testFetchAllUserGameStatuses() {
        assertTrue(0 == 0);
    }

    @Test
    public void testVerifyRegistrationForGame() {
        assertTrue(0 == 0);
    }

    @Test
    public void testVerifyGameInProgress() {

    }

    @Test
    public void testFetchTable() {

    }

    @Test
    public void testFetchPocketCards() {

    }

    @Test
    public void testCheck() {

    }

    @Test
    public void testFetchFlopCards() {

    }

    @Test
    public void testFetchRiverCard() {

    }

    @Test
    public void testFetchTurnCard() {

    }

    @Test
    public void testFetchOptionalShowCards() {

    }

    @Test
    public void testFetchRequiredShowCards() {

    }

    @Test
    public void testGetGameBso() {

    }

    @Test
    public void testSetGameBso() {

    }

    @Test
    public void testGetGameEventBso() {

    }

    @Test
    public void testSetGameEventBso() {

    }

    @Test
    public void testGetEventManager() {

    }

    @Test
    public void testSetEventManager() {

    }

}
