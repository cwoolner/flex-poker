package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.PotBso;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.util.OpenPotPredicate;

@Service
public class PotBsoImpl implements PotBso {

    @Override
    public Set<Pot> calculatePotsAfterRound(Table table) {
        Set<Integer> chipsInFrontSet = new HashSet<>();
        for (Seat seat : table.getSeats()) {
            if (seat.getChipsInFront() != 0) {
                chipsInFrontSet.add(seat.getChipsInFront());
            }
        }

        List<Integer> chipsInFrontList = new ArrayList<Integer>(chipsInFrontSet);
        Collections.sort(chipsInFrontList);

        List<Integer> maxContributionPerSeatPerPot = new ArrayList<Integer>();
        maxContributionPerSeatPerPot.add(chipsInFrontList.get(0));
        for (int i = 0; i < chipsInFrontList.size() - 1; i++) {
            maxContributionPerSeatPerPot.add(
                    chipsInFrontList.get(i + 1) - chipsInFrontList.get(i));
        }

        Set<Pot> pots = new HashSet<>(table.getCurrentHand().getPots());

        for (Integer chipsPerLevel : maxContributionPerSeatPerPot) {
            Pot pot = fetchOpenPot(pots);
            if (pot.getAmount() == 0) {
                pots.add(pot);
            }

            for (Seat seat : table.getSeats()) {
                if (seat.getChipsInFront() == 0) {
                    continue;
                }

                if (!pot.getSeats().contains(seat) && seat.isStillInHand()) {
                    pot.getSeats().add(seat);
                }
                pot.addChips(chipsPerLevel);
                seat.setChipsInFront(seat.getChipsInFront() - chipsPerLevel);
                if (seat.isAllIn()) {
                    pot.closePot();
                }
            }
        }
        
        return pots;
    }

    @Override
    public Set<Seat> determineWinners(Table table, Set<Seat> seats, List<HandEvaluation> winningHands) {
        Set<Seat> winners = new HashSet<>();
        HandEvaluation topAssignedHand = null;
        for (HandEvaluation winningHand : winningHands) {
            for (Seat seat : seats) {
                if (seat.getUserGameStatus().getUser().equals(winningHand.getUser())
                        && (topAssignedHand == null || topAssignedHand.compareTo(winningHand) == 0)) {
                    topAssignedHand = winningHand;
                    winners.add(seat);
                }
            }
        }
        return winners;
    }

    private Pot fetchOpenPot(Set<Pot> pots) {
        Pot pot = (Pot) CollectionUtils.find(pots, new OpenPotPredicate());
        if (pot == null) {
            pot = new Pot();
        }
        return pot;
    }

}
