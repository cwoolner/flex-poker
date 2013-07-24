package com.flexpoker.bso.mock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.PotBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.Pot;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Service
public class PotBsoMock implements PotBso {

    @Override
    public void removeSeatFromPots(Game game, Table table, Seat seat) {

    }

    @Override
    public void createNewHandPot(Game game, Table table) {

    }

    @Override
    public List<Pot> fetchAllPots(Game game, Table table) {
        return null;
    }

    @Override
    public void calculatePotsAfterRound(Game game, Table table) {

    }

    @Override
    public void setWinners(Game game, Table table, List<HandEvaluation> winningHands) {

    }

    @Override
    public void removeTable(Game game, Table table) {

    }

    @Override
    public void removeGame(Game game) {

    }

}
