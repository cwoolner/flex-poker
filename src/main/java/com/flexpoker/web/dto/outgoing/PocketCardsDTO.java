package com.flexpoker.web.dto.outgoing;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PocketCardsDTO {

    private final int cardId1;

    private final int cardId2;

    private final UUID tableId;

    public PocketCardsDTO(int cardId1, int cardId2, UUID tableId) {
        this.cardId1 = cardId1;
        this.cardId2 = cardId2;
        this.tableId = tableId;
    }

    @JsonProperty
    public int getCardId1() {
        return cardId1;
    }

    @JsonProperty
    public int getCardId2() {
        return cardId2;
    }

    @JsonProperty
    public UUID getTableId() {
        return tableId;
    }

}
