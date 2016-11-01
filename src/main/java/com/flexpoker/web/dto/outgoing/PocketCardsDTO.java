package com.flexpoker.web.dto.outgoing;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PocketCardsDTO {

    private final UUID handId;

    private final int cardId1;

    private final int cardId2;

    public PocketCardsDTO(UUID handId, int cardId1, int cardId2) {
        this.handId = handId;
        this.cardId1 = cardId1;
        this.cardId2 = cardId2;
    }

    @JsonProperty
    public UUID getHandId() {
        return handId;
    }

    @JsonProperty
    public int getCardId1() {
        return cardId1;
    }

    @JsonProperty
    public int getCardId2() {
        return cardId2;
    }

}
