package com.flexpoker.controller;

import com.flexpoker.model.Game;

public interface EventManager {

    void sendGamesUpdatedEvent();

    void sendUserJoinedEvent(Game game);

    void sendChatEvent(String username, String text) ;

    void sendGameStartingEvent(Game game);

    void sendGameInProgressEvent(Game game);

}
