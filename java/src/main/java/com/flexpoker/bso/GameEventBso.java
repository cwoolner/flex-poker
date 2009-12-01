package com.flexpoker.bso;

import java.util.List;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameEventBso {

    boolean addUserToGame(User user, Game game);

    boolean verifyRegistration(User user, Game game);

    void startNewHand(Game game, Table table);

    boolean verifyGameInProgress(User user, Game game);

    HandState check(Game game, Table table, User user);

    HandState fold(Game game, Table table, User user);

    List<PocketCards> fetchRequiredShowCards(Game game, Table table);

    List<PocketCards> fetchOptionalShowCards(Game game, Table table);

}
