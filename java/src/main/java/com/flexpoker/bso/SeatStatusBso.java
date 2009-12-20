package com.flexpoker.bso;

import com.flexpoker.model.Table;


public interface SeatStatusBso {

    void setStatusForNewGame(Table table);

    void setStatusForNewRound(Table table);

    void setStatusForNewHand(Table table);

    void setStatusForEndOfHand(Table table);

}
