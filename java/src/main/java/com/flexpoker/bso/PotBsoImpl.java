package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.flexpoker.model.Game;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Service("potBso")
public class PotBsoImpl implements PotBso {

    private Map<Game, Map<Table, List<Pot>>> gameToTableToPotsMap
            = new HashMap<Game, Map<Table,List<Pot>>>();

    @Override
    public void calculatePotsAfterRound(Game game, Table table) {
        // TODO Auto-generated method stub

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
        List<Pot> pots = gameToTableToPotsMap.get(game).get(table);

        for (int i = 0; i < winningHands.size(); i++) {
            int j;
            for (j = i; j < winningHands.size() - 1; j++) {
                if (winningHands.get(j).compareTo(winningHands.get(j + 1)) != 0) {
                    break;
                }
            }

            while (!areAllWinnersAssigned(pots)) {
                assignWinners(winningHands.subList(i, j + 1), pots);
            }
        }
    }

    private boolean areAllWinnersAssigned(List<Pot> pots) {
        for (Pot pot : pots) {
            if (CollectionUtils.isEmpty(pot.getWinners())) {
                return false;
            }
        }

        return true;
    }

    private void assignWinners(List<HandEvaluation> winningHands, List<Pot> pots) {
        for (Pot pot : pots) {
            if (CollectionUtils.isNotEmpty(pot.getWinners())) {
                break;
            }

            pot.setWinners(new ArrayList<Seat>());

            for (HandEvaluation winningHand : winningHands) {
                for (Seat seat : pot.getSeats()) {
                    if (seat.getUserGameStatus().getUser()
                            .equals(winningHand.getUser())) {
                        pot.getWinners().add(seat);
                    }
                }
            }
        }
    }

}
