package com.flexpoker.model;

import java.util.ArrayList;
import java.util.List;

public class CommonCards {

    private List<Card> cards = new ArrayList<Card>();

    public CommonCards(FlopCards flopCards, TurnCard turnCard, RiverCard riverCard) {
        cards.add(flopCards.getCard1());
        cards.add(flopCards.getCard2());
        cards.add(flopCards.getCard3());
        cards.add(turnCard.getCard());
        cards.add(riverCard.getCard());
    }

    public List<Card> getCards() {
        return cards;
    }

}
