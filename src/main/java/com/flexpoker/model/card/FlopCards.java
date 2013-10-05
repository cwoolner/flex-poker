package com.flexpoker.model.card;

public class FlopCards {

    private final Card card1;

    private final Card card2;

    private final Card card3;

    public FlopCards(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public Card getCard3() {
        return card3;
    }

}
