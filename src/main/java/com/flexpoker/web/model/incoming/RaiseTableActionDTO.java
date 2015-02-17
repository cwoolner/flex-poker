package com.flexpoker.web.model.incoming;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RaiseTableActionDTO extends DefaultTableActionDTO {

    private final int raiseToAmount;

    @JsonCreator
    public RaiseTableActionDTO(@JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "raiseToAmount") int raiseToAmount) {
        super(gameId, tableId);
        this.raiseToAmount = raiseToAmount;
    }

    public int getRaiseToAmount() {
        return raiseToAmount;
    }

}
