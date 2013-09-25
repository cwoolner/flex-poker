package com.flexpoker.web.model;

import java.util.UUID;

public class PocketCardsViewModel {

    private final int cardId1;

    private final int cardId2;

    private final UUID tableId;

    public PocketCardsViewModel(int cardId1, int cardId2, UUID tableId) {
        this.cardId1 = cardId1;
        this.cardId2 = cardId2;
        this.tableId = tableId;
    }

    public Integer getCardId1() {
        return cardId1;
    }

    public Integer getCardId2() {
        return cardId2;
    }

    public UUID getTableId() {
        return tableId;
    }

}
