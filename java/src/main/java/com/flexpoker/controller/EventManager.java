package com.flexpoker.controller;

import com.flexpoker.model.Game;
import com.flexpoker.model.User;

public interface EventManager {

    void sendGamesUpdatedEvent();

    void sendUserJoinedEvent(User user, Game game);

    void sendChatEvent(String username, String text) ;

    void sendGameStartingEvent(Game game);

}
