package com.flexpoker.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurnCard {

    private final Card card;

    @JsonCreator
    public TurnCard(@JsonProperty(value = "card") Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

}
