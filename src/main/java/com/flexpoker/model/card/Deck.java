package com.flexpoker.model.card;

import java.util.List;

public class Deck {

    private final FlopCards flopCards;

    private final TurnCard turnCard;

    private final RiverCard riverCard;

    private final List<PocketCards> pocketCards;

    public Deck(FlopCards flopCards, TurnCard turnCard, RiverCard riverCard,
            List<PocketCards> pocketCards) {
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.pocketCards = pocketCards;
    }

    public FlopCards getFlopCards() {
        return flopCards;
    }

    public TurnCard getTurnCard() {
        return turnCard;
    }

    public RiverCard getRiverCard() {
        return riverCard;
    }

    public PocketCards getPocketCards(int seatPosition) {
        // TODO: change this to get the cards correctly based on the dealer
        //       position instead of just returning the cards at the seat
        //       position specified
        return pocketCards.get(seatPosition);
    }

}
