package com.flexpoker.model;

public class TurnCard {

    private Card card;

    public TurnCard(Card card) {
        this.card = card;
    }

    public TurnCard() {}

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
