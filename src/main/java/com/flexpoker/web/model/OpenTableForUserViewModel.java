package com.flexpoker.web.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenTableForUserViewModel {

    @JsonProperty
    private final UUID gameId;

    @JsonProperty
    private final UUID tableId;

    public OpenTableForUserViewModel(UUID gameId, UUID tableId) {
        this.gameId = gameId;
        this.tableId = tableId;
    }

}
