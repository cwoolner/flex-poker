package com.flexpoker.bso;

import com.flexpoker.model.Game;
import com.flexpoker.model.User;


public interface GameEventBso {

    void addUserToGame(User user, Game game);

    boolean isGameAtMaxPlayers(Game game);

}
