package com.flexpoker.core.api.pot;

import java.util.List;
import java.util.Set;

import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface DeterminePotWinnersQuery {

    Set<Seat> execute(Table table, Set<Seat> seats,
            List<HandEvaluation> winningHands);

}
