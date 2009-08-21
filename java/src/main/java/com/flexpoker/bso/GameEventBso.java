package com.flexpoker.bso;

import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

public interface GameEventBso {

    void addUserToGame(User user, Game game);

    boolean isGameAtMaxPlayers(Game game);

    void verifyRegistration(User user, Game game);

    boolean haveAllPlayersVerifiedRegistration(Game game);

    PocketCards fetchPocketCards(User user, Table table);

    void startNewHand(Table table);

    void startNewHandForAllTables(Game game);

    void verifyGameInProgress(User user, Game game);

    boolean haveAllPlayersVerifiedGameInProgress(Game game);

    boolean isUserAllowedToPerformAction(String action, User user, Table table);

    void check(User user, Table table);

    boolean isRoundComplete(Table table);

    boolean isHandComplete(Table table);

    void updateState(Table table);

    boolean isFlopDealt(Table table);

    boolean isTurnDealt(Table table);

    boolean isRiverDealt(Table table);

}
