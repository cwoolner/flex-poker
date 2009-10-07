package com.flexpoker.controller;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

public interface EventManager {

    void sendGamesUpdatedEvent();

    void sendUserJoinedEvent(Game game, String username, boolean gameAtUserMax);

    void sendChatEvent(String username, String text) ;

    void sendGameStartingEvent(Game game);

    void sendGameInProgressEvent(Game game);

    void sendNewHandStartingEvent(Game game, Table table);

    void sendDealFlopEvent(Game game, Table table);

    void sendDealTurnEvent(Game game, Table table);

    void sendDealRiverEvent(Game game, Table table);

    void sendHandCompleteEvent(Game game, Table table);

    void sendUserActedEvent(Game game, Table table);

}
