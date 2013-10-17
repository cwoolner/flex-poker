package com.flexpoker.bso.api;

import java.util.List;
import java.util.Set;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface PotBso {

    void removeSeatFromPots(Game game, Table table, Seat seat);

    void createNewHandPot(Game game, Table table);

    List<Pot> fetchAllPots(Game game, Table table);

    void calculatePotsAfterRound(Game game, Table table);

    Set<Seat> determineWinners(Table table, Set<Seat> seats,
            List<HandEvaluation> winningHands);

}
