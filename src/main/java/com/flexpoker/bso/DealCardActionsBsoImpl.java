package com.flexpoker.bso;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.DealCardActionsBso;
import com.flexpoker.bso.api.DeckBso;
import com.flexpoker.bso.api.ValidationBso;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;

@Service
public class DealCardActionsBsoImpl implements DealCardActionsBso {

    private DeckBso deckBso;

    private ValidationBso validationBso;
    
    @Inject
    public DealCardActionsBsoImpl(DeckBso deckBso, ValidationBso validationBso) {
        this.deckBso = deckBso;
        this.validationBso = validationBso;
    }

    @Override
    public PocketCards fetchPocketCards(User user, Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.POCKET_CARDS_DEALT.ordinal()) {
            return deckBso.fetchPocketCards(user, game, table);
        }

        throw new FlexPokerException("You are not allowed to fetch your pocket cards");
    }

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.FLOP_DEALT.ordinal()) {
            return deckBso.fetchFlopCards(game, table);
        }

        throw new FlexPokerException("You are not allowed to fetch the flop cards.");
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.RIVER_DEALT.ordinal()) {
            return deckBso.fetchRiverCard(game, table);
        }

        throw new FlexPokerException("You are not allowed to fetch the river card.");
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.TURN_DEALT.ordinal()) {
            return deckBso.fetchTurnCard(game, table);
        }

        throw new FlexPokerException("You are not allowed to fetch the turn card.");
    }

    private HandDealerState determineHandDealerState(Game game, Table table) {
        RealTimeHand realTimeHand = game.getRealTimeHand(table);
        HandDealerState handDealerState = realTimeHand.getHandDealerState();
        return handDealerState;
    }

}
