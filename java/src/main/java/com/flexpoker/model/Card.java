package com.flexpoker.model;

public class Card {

    private Integer id;

    private CardRank cardRank;

    private CardSuit cardSuit;

    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CardRank getCardRank() {
        return cardRank;
    }

    public void setCardRank(CardRank cardRank) {
        this.cardRank = cardRank;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    public void setCardSuit(CardSuit cardSuit) {
        this.cardSuit = cardSuit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
