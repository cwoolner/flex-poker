package com.flexpoker.test.util.datageneration;

import java.util.Arrays;

import com.flexpoker.table.command.CardsUsedInHand;
import com.flexpoker.table.command.PocketCards;

public class DeckGenerator {

    public static CardsUsedInHand createDeck() {
        return new CardsUsedInHand(CardGenerator.createFlopCards(),
                CardGenerator.createTurnCard(), CardGenerator.createRiverCard(),
                Arrays.asList(new PocketCards[] { CardGenerator.createPocketCards1(),
                        CardGenerator.createPocketCards2() }));
    }
}
