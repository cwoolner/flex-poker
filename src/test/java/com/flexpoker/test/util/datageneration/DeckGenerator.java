package com.flexpoker.test.util.datageneration;

import java.util.Arrays;

import com.flexpoker.model.card.Deck;
import com.flexpoker.model.card.PocketCards;

public class DeckGenerator {

    public static Deck createDeck() {
        return new Deck(CardGenerator.createFlopCards(),
                CardGenerator.createTurnCard(), CardGenerator.createRiverCard(),
                Arrays.asList(new PocketCards[]{CardGenerator.createPocketCards()}));
    }
}
