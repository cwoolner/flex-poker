package com.flexpoker.bso;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.DealCardActionsBso;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.Hand;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.Table;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

@Service
public class DealCardActionsBsoImpl implements DealCardActionsBso {

    @Override
    public FlopCards fetchFlopCards(Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.FLOP_DEALT.ordinal()) {
            return table.getCurrentHand().getDeck().getFlopCards();
        }

        throw new FlexPokerException("You are not allowed to fetch the flop cards.");
    }

    @Override
    public RiverCard fetchRiverCard(Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.RIVER_DEALT.ordinal()) {
            return table.getCurrentHand().getDeck().getRiverCard();
        }

        throw new FlexPokerException("You are not allowed to fetch the river card.");
    }

    @Override
    public TurnCard fetchTurnCard(Game game, Table table) {
        HandDealerState handDealerState = determineHandDealerState(game, table);
        if (handDealerState.ordinal() >= HandDealerState.TURN_DEALT.ordinal()) {
            return table.getCurrentHand().getDeck().getTurnCard();
        }

        throw new FlexPokerException("You are not allowed to fetch the turn card.");
    }

    private HandDealerState determineHandDealerState(Game game, Table table) {
        Hand realTimeHand = table.getCurrentHand();
        HandDealerState handDealerState = realTimeHand.getHandDealerState();
        return handDealerState;
    }

}
