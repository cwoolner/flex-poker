package com.flexpoker.core.api.deck;

import com.flexpoker.model.card.Deck;

public interface CreateShuffledDeckCommand {

    Deck execute(int numberOfPlayers);

}
