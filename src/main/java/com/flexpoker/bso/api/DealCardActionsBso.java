package com.flexpoker.bso.api;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.Game;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;


public interface DealCardActionsBso {

    PocketCards fetchPocketCards(User user, Game game, Table table);

    FlopCards fetchFlopCards(Game game, Table table);

    RiverCard fetchRiverCard(Game game, Table table);

    TurnCard fetchTurnCard(Game game, Table table);

}
