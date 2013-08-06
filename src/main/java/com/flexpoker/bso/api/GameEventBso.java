package com.flexpoker.bso.api;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameEventBso {

    boolean addUserToGame(User user, Game game);

    boolean verifyRegistration(User user, Game game);

    boolean verifyGameInProgress(User user, Game game);

    boolean verifyReadyToStartNewHand(User user, Game game, Table table);

}
