package com.flexpoker.bso;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameEventBso {

    boolean addUserToGame(User user, Game game);

    boolean verifyRegistration(User user, Game game);

    boolean verifyGameInProgress(User user, Game game);

    boolean verifyReadyToStartNewHand(User user, Game game, Table table);

    HandState check(Game game, Table table, User user);

    HandState fold(Game game, Table table, User user);

    HandState call(Game game, Table table, User user);

}
