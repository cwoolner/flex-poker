package com.flexpoker.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlopCards {

    private final Card card1;

    private final Card card2;

    private final Card card3;

    @JsonCreator
    public FlopCards(@JsonProperty(value = "card1") Card card1,
            @JsonProperty(value = "card2") Card card2,
            @JsonProperty(value = "card3") Card card3) {
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
