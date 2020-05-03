package com.flexpoker.web.dto.outgoing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDTO {

    private final int id;

    @JsonCreator
    public CardDTO(@JsonProperty(value = "id") int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
