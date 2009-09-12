package com.flexpoker.bso;

import java.util.Map;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameEventType;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
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

    boolean isUserAllowedToPerformAction(GameEventType action, User user,
            Table table);

    boolean isRoundComplete(Table table);

    boolean isHandComplete(Table table);

    void updateCheckState(Table table);

    boolean isFlopDealt(Table table);

    boolean isTurnDealt(Table table);

    boolean isRiverDealt(Table table);

    FlopCards fetchFlopCards(Table table);

    RiverCard fetchRiverCard(Table table);

    TurnCard fetchTurnCard(Table table);

    void setRoundComplete(Table table, boolean b);

    Map<Integer, PocketCards> fetchRequiredShowCards(Table table);

    Map<Integer, PocketCards> fetchOptionalShowCards(Table table);

}
