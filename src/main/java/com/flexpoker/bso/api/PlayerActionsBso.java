
package com.flexpoker.bso.api;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface PlayerActionsBso {

    HandState check(Game game, Table table, User user);

    HandState fold(Game game, Table table, User user);

    HandState call(Game game, Table table, User user);

    HandState raise(Game game, Table table, User user, String raiseAmount);

}
