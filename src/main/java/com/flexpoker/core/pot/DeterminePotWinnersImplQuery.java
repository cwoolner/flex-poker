package com.flexpoker.core.pot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flexpoker.config.Query;
import com.flexpoker.core.api.pot.DeterminePotWinnersQuery;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Query
public class DeterminePotWinnersImplQuery implements DeterminePotWinnersQuery {

    @Override
    public Set<Seat> execute(Table table, Set<Seat> seats,
            List<HandEvaluation> winningHands) {
        Set<Seat> winners = new HashSet<>();
        HandEvaluation topAssignedHand = null;
        for (HandEvaluation winningHand : winningHands) {
            for (Seat seat : seats) {
                if (seat.getUserGameStatus().getUser().getId()
                        .equals(winningHand.getPlayerId())
                        && (topAssignedHand == null || topAssignedHand
                                .compareTo(winningHand) == 0)) {
                    topAssignedHand = winningHand;
                    winners.add(seat);
                }
            }
        }
        return winners;
    }

}
