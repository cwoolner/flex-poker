package com.flexpoker.bso.api;

import java.util.List;
import java.util.Set;

import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface PotBso {

    Set<Pot> calculatePotsAfterRound(Table table);

    Set<Seat> determineWinners(Table table, Set<Seat> seats,
            List<HandEvaluation> winningHands);

}
