package com.flexpoker.bso;

import org.springframework.stereotype.Service;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service("dealCardActionsBso")
public class DealCardActionsBsoImpl implements DealCardActionsBso {

    private DeckBso deckBso;

    @Override
    public PocketCards fetchPocketCards(User user, Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return deckBso.fetchPocketCards(user, table);
    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return deckBso.fetchFlopCards(table);
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return deckBso.fetchRiverCard(table);
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        return deckBso.fetchTurnCard(table);
    }

    public DeckBso getDeckBso() {
        return deckBso;
    }

    public void setDeckBso(DeckBso deckBso) {
        this.deckBso = deckBso;
    }

}
