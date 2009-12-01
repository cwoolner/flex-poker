package com.flexpoker.controller.mock;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.flexpoker.controller.EventManager;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;

@Controller
public class EventManagerMock implements EventManager {

    @Override
    public void sendChatEvent(String username, String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendGameInProgressEvent(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendGameStartingEvent(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendGamesUpdatedEvent() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendNewHandStartingEvent(Game game, Table table) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendUserJoinedEvent(Game game, String username,
            boolean gameAtUserMax) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendNewHandStartingEventForAllTables(Game game, List<Table> tables) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendCheckEvent(Game game, Table table, HandState handState, String username) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendFoldEvent(Game game, Table table, HandState handState, String username) {
        // TODO Auto-generated method stub

    }

}
