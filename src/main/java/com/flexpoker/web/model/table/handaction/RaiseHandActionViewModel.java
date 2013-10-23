package com.flexpoker.web.model.table.handaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RaiseHandActionViewModel extends BaseHandActionViewModel {

    private final int raiseToAmount;
    
    @JsonCreator
    public RaiseHandActionViewModel(
            @JsonProperty(value = "gameId") String gameId,
            @JsonProperty(value = "tableId") String tableId,
            @JsonProperty(value = "raiseToAmount") int raiseToAmount) {
        super(gameId, tableId);
        this.raiseToAmount = raiseToAmount;
    }

    public int getRaiseToAmount() {
        return raiseToAmount;
    }

}
