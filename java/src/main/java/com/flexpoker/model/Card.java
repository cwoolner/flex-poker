package com.flexpoker.model;

public class Card {

    private int id;

    private CardRank cardRank;

    private CardSuit cardSuit;

    public Card() {}

    public Card(int id, CardRank cardRank, CardSuit cardSuit) {
        this.id = id;
        this.cardRank = cardRank;
        this.cardSuit = cardSuit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CardRank getCardRank() {
        return cardRank;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

}
