package com.flexpoker.bso;

import java.util.Map;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameEventBso {

    boolean addUserToGame(User user, Game game);

    boolean verifyRegistration(User user, Game game);

    void startNewHand(Game game, Table table);

    void startNewHandForAllTables(Game game);

    boolean verifyGameInProgress(User user, Game game);

    HandState check(Game game, Table table, User user);

    Map<Integer, PocketCards> fetchRequiredShowCards(Game game, Table table);

    Map<Integer, PocketCards> fetchOptionalShowCards(Game game, Table table);

}
