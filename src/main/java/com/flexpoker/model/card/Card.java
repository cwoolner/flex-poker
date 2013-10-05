package com.flexpoker.model.card;

public class Card implements Comparable<Card> {

    private final int id;

    private final CardRank cardRank;

    private final CardSuit cardSuit;

    public Card(int id, CardRank cardRank, CardSuit cardSuit) {
        this.id = id;
        this.cardRank = cardRank;
        this.cardSuit = cardSuit;
    }

    public int getId() {
        return id;
    }

    public CardRank getCardRank() {
        return cardRank;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    @Override
    public int compareTo(Card otherCard) {
        return cardRank.compareTo(otherCard.cardRank);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
