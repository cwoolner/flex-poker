package com.flexpoker.controller;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

public interface EventManager {

    void sendGamesUpdatedEvent();

    void sendUserJoinedEvent(Game game);

    void sendChatEvent(String username, String text) ;

    void sendGameStartingEvent(Game game);

    void sendGameInProgressEvent(Game game);

    void sendNewHandStartingEvent(Table table);

    void sendDealFlopEvent(Table table);

    void sendDealTurnEvent(Table table);

    void sendDealRiverEvent(Table table);

    void sendHandCompleteEvent(Table table);

}
