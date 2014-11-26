package com.flexpoker.model.card;

import java.util.List;

public class CardsUsedInHand {

    private final FlopCards flopCards;

    private final TurnCard turnCard;

    private final RiverCard riverCard;

    private final List<PocketCards> pocketCards;

    public CardsUsedInHand(FlopCards flopCards, TurnCard turnCard, RiverCard riverCard,
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

    public List<PocketCards> getPocketCards() {
        return pocketCards;
    }

}
