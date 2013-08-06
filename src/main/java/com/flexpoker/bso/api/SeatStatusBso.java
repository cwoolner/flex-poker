package com.flexpoker.bso.api;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

public interface SeatStatusBso {

    void setStatusForNewGame(Game game, Table table);

    void setStatusForNewRound(Game game, Table table);

    void setStatusForNewHand(Game game, Table table);

    void setStatusForEndOfHand(Game game, Table table);

}
