package com.flexpoker.bso;

import com.flexpoker.model.FlopCards;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RiverCard;
import com.flexpoker.model.Table;
import com.flexpoker.model.TurnCard;
import com.flexpoker.model.User;


public interface DeckBso {

    void shuffleDeck(Table table);

    void removeDeck(Table table);

    PocketCards fetchPocketCards(User user, Table table);

    FlopCards fetchFlopCards(Table table);

    TurnCard fetchTurnCard(Table table);

    RiverCard fetchRiverCard(Table table);

}
