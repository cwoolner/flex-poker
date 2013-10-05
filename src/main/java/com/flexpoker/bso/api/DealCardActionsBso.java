package com.flexpoker.bso.api;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public interface DealCardActionsBso {

    FlopCards fetchFlopCards(Game game, Table table);

    RiverCard fetchRiverCard(Game game, Table table);

    TurnCard fetchTurnCard(Game game, Table table);

}
