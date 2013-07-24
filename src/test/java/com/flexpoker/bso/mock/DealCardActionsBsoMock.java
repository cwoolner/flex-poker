package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.DealCardActionsBso;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service
public class DealCardActionsBsoMock implements DealCardActionsBso {

    @Override
    public PocketCards fetchPocketCards(User user, Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

}
