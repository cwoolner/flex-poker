package com.flexpoker.web.model.outgoing;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenTableForUserDTO {

    @JsonProperty
    private final UUID gameId;

    @JsonProperty
    private final UUID tableId;

    public OpenTableForUserDTO(UUID gameId, UUID tableId) {
        this.gameId = gameId;
        this.tableId = tableId;
    }

}
