package com.flexpoker.game.command.events.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlindAmountsDTO {

    private final int smallBlind;

    private final int bigBlind;

    @JsonCreator
    public BlindAmountsDTO(
            @JsonProperty(value = "smallBlind") int smallBlind,
            @JsonProperty(value = "bigBlind") int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

}
