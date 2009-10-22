package com.flexpoker.bso;

import org.springframework.stereotype.Service;

import com.flexpoker.exception.FlexPokerException;
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

    private RealTimeGameBso realTimeGameBso;

    @Override
    public PocketCards fetchPocketCards(User user, Game game, Table table) {
        HandDealerState handDealerState = realTimeGameBso.get(game)
                .getRealTimeHand(table).getHandDealerState();
        if (handDealerState.ordinal() >= HandDealerState.POCKET_CARDS_DEALT.ordinal()) {
            return deckBso.fetchPocketCards(user, table);
        }

        throw new FlexPokerException("You are not allowed to fetch your pocket cards");
    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        HandDealerState handDealerState = realTimeGameBso.get(game)
                .getRealTimeHand(table).getHandDealerState();
        if (handDealerState.ordinal() >= HandDealerState.FLOP_DEALT.ordinal()) {
            return deckBso.fetchFlopCards(table);
        }

        throw new FlexPokerException("You are not allowed to fetch the flop cards.");
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        HandDealerState handDealerState = realTimeGameBso.get(game)
                .getRealTimeHand(table).getHandDealerState();
        if (handDealerState.ordinal() >= HandDealerState.RIVER_DEALT.ordinal()) {
            return deckBso.fetchRiverCard(table);
        }

        throw new FlexPokerException("You are not allowed to fetch the river card.");
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        HandDealerState handDealerState = realTimeGameBso.get(game)
                .getRealTimeHand(table).getHandDealerState();
        if (handDealerState.ordinal() >= HandDealerState.TURN_DEALT.ordinal()) {
            return deckBso.fetchTurnCard(table);
        }

        throw new FlexPokerException("You are not allowed to fetch the turn card.");
    }

    public DeckBso getDeckBso() {
        return deckBso;
    }

    public void setDeckBso(DeckBso deckBso) {
        this.deckBso = deckBso;
    }

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

}
