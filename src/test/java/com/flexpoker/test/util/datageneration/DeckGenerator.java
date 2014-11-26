package com.flexpoker.test.util.datageneration;

import java.util.Arrays;

import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;

public class DeckGenerator {

    public static CardsUsedInHand createDeck() {
        return new CardsUsedInHand(CardGenerator.createFlopCards(),
                CardGenerator.createTurnCard(), CardGenerator.createRiverCard(),
                Arrays.asList(new PocketCards[]{CardGenerator.createPocketCards()}));
    }
}
