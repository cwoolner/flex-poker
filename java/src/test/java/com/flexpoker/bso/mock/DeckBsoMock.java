package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.DeckBso;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service("deckBsoMock")
public class DeckBsoMock implements DeckBso {

    @Override
    public FlopCards fetchFlopCards(Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PocketCards fetchPocketCards(User user, Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RiverCard fetchRiverCard(Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TurnCard fetchTurnCard(Table table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeDeck(Table table) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shuffleDeck(Table table) {
        // TODO Auto-generated method stub

    }

}
