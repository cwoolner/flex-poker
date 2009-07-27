package com.flexpoker.bso;

import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameEventBso {

    void addUserToGame(User user, Game game);

    boolean isGameAtMaxPlayers(Game game);

    void verifyRegistration(User user, Game game);

    boolean areAllPlayerRegistrationsVerified(Game game);

    PocketCards fetchPocketCards(User user, Table table);

    void startNewHand(Table table);

    void startNewHandForAllTables(Game game);

    void verifyGameInProgress(User user, Game game);

    boolean haveAllPlayersVerifiedGameInProgress(Game game);

}
