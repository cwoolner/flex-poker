package com.flexpoker.test.util.datageneration;

import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.model.card.CardSuit;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;

public class CardGenerator {

    private static final Card CARD_1 = new Card(1, CardRank.ACE, CardSuit.CLUBS);

    private static final Card CARD_2 = new Card(2, CardRank.TWO, CardSuit.CLUBS);

    private static final Card CARD_3 = new Card(3, CardRank.THREE, CardSuit.CLUBS);

    private static final Card CARD_4 = new Card(4, CardRank.FOUR, CardSuit.CLUBS);

    private static final Card CARD_5 = new Card(5, CardRank.FIVE, CardSuit.CLUBS);

    private static final Card CARD_6 = new Card(6, CardRank.SIX, CardSuit.CLUBS);

    private static final Card CARD_7 = new Card(7, CardRank.SEVEN, CardSuit.CLUBS);

    private static final Card CARD_8 = new Card(8, CardRank.EIGHT, CardSuit.CLUBS);

    private static final Card CARD_9 = new Card(9, CardRank.NINE, CardSuit.CLUBS);

    private static final PocketCards pocketCards1 = new PocketCards(CARD_6, CARD_7);

    private static final PocketCards pocketCards2 = new PocketCards(CARD_8, CARD_9);

    public static FlopCards createFlopCards() {
        return new FlopCards(CARD_1, CARD_2, CARD_3);
    }

    public static TurnCard createTurnCard() {
        return new TurnCard(CARD_4);
    }

    public static RiverCard createRiverCard() {
        return new RiverCard(CARD_5);
    }

    public static PocketCards createPocketCards1() {
        return pocketCards1;
    }

    public static PocketCards createPocketCards2() {
        return pocketCards2;
    }

}
