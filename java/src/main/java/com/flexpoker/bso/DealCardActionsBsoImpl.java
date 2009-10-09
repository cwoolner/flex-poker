package com.flexpoker.bso;

import org.springframework.stereotype.Service;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service("dealCardActionsBso")
public class DealCardActionsBsoImpl implements DealCardActionsBso {

    private DeckBso deckBso;

    private RealTimeHandBso realTimeHandBso;

    @Override
    public PocketCards fetchPocketCards(User user, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        realTimeHandBso.get(table).setHandDealerState(HandDealerState.POCKET_CARDS_DEALT);
        return deckBso.fetchPocketCards(user, table);
    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        realTimeHandBso.get(table).setHandDealerState(HandDealerState.FLOP_DEALT);
        return deckBso.fetchFlopCards(table);
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        realTimeHandBso.get(table).setHandDealerState(HandDealerState.RIVER_DEALT);
        return deckBso.fetchRiverCard(table);
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        // TODO: This should have an additional "can they do this?" check.
        realTimeHandBso.get(table).setHandDealerState(HandDealerState.TURN_DEALT);
        return deckBso.fetchTurnCard(table);
    }

    public DeckBso getDeckBso() {
        return deckBso;
    }

    public void setDeckBso(DeckBso deckBso) {
        this.deckBso = deckBso;
    }

    public RealTimeHandBso getRealTimeHandBso() {
        return realTimeHandBso;
    }

    public void setRealTimeHandBso(RealTimeHandBso realTimeHandBso) {
        this.realTimeHandBso = realTimeHandBso;
    }

}
