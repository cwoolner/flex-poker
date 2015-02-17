package com.flexpoker.web.model.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {

    @JsonProperty
    private int id;

    public CardDTO(int id) {
        this.id = id;
    }

}
