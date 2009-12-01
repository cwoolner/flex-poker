package com.flexpoker.controller;

import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;

public interface EventManager {

    void sendGamesUpdatedEvent();

    void sendUserJoinedEvent(Game game, String username, boolean gameAtUserMax);

    void sendChatEvent(String username, String text) ;

    void sendGameStartingEvent(Game game);

    void sendGameInProgressEvent(Game game);

    void sendNewHandStartingEvent(Game game, Table table);

    void sendNewHandStartingEventForAllTables(Game game, List<Table> tables);

    void sendCheckEvent(Game game, Table table, HandState handState, String username);

    void sendFoldEvent(Game game, Table table, HandState handState, String username);

}
