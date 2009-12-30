package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.util.OpenPotPredicate;

@Service("potBso")
public class PotBsoImpl implements PotBso {

    private Map<Game, Map<Table, List<Pot>>> gameToTableToPotsMap
            = new HashMap<Game, Map<Table,List<Pot>>>();

    @Override
    public void calculatePotsAfterRound(Game game, Table table) {
        Set<Integer> chipsInFrontSet = new HashSet<Integer>();
        for (Seat seat : table.getSeats()) {
            if (seat.getChipsInFront() != 0) {
                chipsInFrontSet.add(seat.getChipsInFront());
            }
        }

        if (chipsInFrontSet.isEmpty()) {
            // nothing to do since there are no chips to add to pots
            return;
        }

        List<Integer> chipsInFrontList = new ArrayList<Integer>(chipsInFrontSet);
        Collections.sort(chipsInFrontList);

        List<Integer> maxContributionPerSeatPerPot = new ArrayList<Integer>();
        maxContributionPerSeatPerPot.add(chipsInFrontList.get(0));
        for (int i = 0; i < chipsInFrontList.size() - 1; i++) {
            maxContributionPerSeatPerPot.add(
                    chipsInFrontList.get(i + 1) - chipsInFrontList.get(i));
        }

        List<Pot> pots = fetchAllPots(game, table);

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
                pot.setAmount(pot.getAmount() + chipsPerLevel);
                seat.setChipsInFront(seat.getChipsInFront() - chipsPerLevel);
                if (seat.isAllIn()) {
                    pot.setOpen(false);
                }
            }
        }
    }

    @Override
    public void createNewHandPot(Game game, Table table) {
        if (gameToTableToPotsMap.get(game) == null) {
            gameToTableToPotsMap.put(game, new HashMap<Table, List<Pot>>());
        }
        gameToTableToPotsMap.get(game).put(table, new ArrayList<Pot>());
    }

    @Override
    public List<Pot> fetchAllPots(Game game, Table table) {
        return gameToTableToPotsMap.get(game).get(table);
    }

    @Override
    public void removeGame(Game game) {
        gameToTableToPotsMap.remove(game);
    }

    @Override
    public void removeSeatFromPots(Game game, Table table, Seat seat) {
        List<Pot> pots = gameToTableToPotsMap.get(game).get(table);
        for (Pot pot : pots) {
            pot.getSeats().remove(seat);
        }
    }

    @Override
    public void removeTable(Game game, Table table) {
        gameToTableToPotsMap.get(game).remove(table);
    }

    @Override
    public void setWinners(Game game, Table table, List<HandEvaluation> winningHands) {
        Collections.sort(winningHands);
        Collections.reverse(winningHands);

        for (Pot pot : fetchAllPots(game, table)) {
            pot.setWinners(new ArrayList<Seat>());
            HandEvaluation topAssignedHand = null;
            for (HandEvaluation hand : winningHands) {
                for (Seat seat : pot.getSeats()) {
                    if (seat.getUserGameStatus().getUser().equals(hand.getUser())
                            && (topAssignedHand == null
                                    || topAssignedHand.compareTo(hand) == 0)) {
                        topAssignedHand = hand;
                        pot.getWinners().add(seat);
                    }
                }
            }
        }
    }

    private Pot fetchOpenPot(List<Pot> pots) {
        Pot pot = (Pot) CollectionUtils.find(pots, new OpenPotPredicate());
        if (pot == null) {
            pot = new Pot();
            pot.setOpen(true);
            pot.setSeats(new ArrayList<Seat>());
        }
        return pot;
    }

}
