package com.flexpoker.web.dto.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {

    private final int id;

    public CardDTO(int id) {
        this.id = id;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

}
