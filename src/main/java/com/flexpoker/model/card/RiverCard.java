package com.flexpoker.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RiverCard {

    private final Card card;

    @JsonCreator
    public RiverCard(@JsonProperty(value = "card") Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

}
