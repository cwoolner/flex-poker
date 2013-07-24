package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.DeckBso;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service
public class DeckBsoMock implements DeckBso {

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        return new FlopCards();
    }

    @Override
    public PocketCards fetchPocketCards(User user, Game game, Table table) {
        return new PocketCards();
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        return new RiverCard();
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        return new TurnCard();
    }

    @Override
    public void removeDeck(Game game, Table table) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shuffleDeck(Game game, Table table) {
        // TODO Auto-generated method stub

    }

}
