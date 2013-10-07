package com.flexpoker.web.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardViewModel {

    @JsonProperty
    private int id;

    public CardViewModel(int id) {
        this.id = id;
    }

}
