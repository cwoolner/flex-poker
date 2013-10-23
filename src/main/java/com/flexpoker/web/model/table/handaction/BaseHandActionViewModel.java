package com.flexpoker.web.model.table.handaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseHandActionViewModel {

    private final String gameId;

    private final String tableId;

    @JsonCreator
    public BaseHandActionViewModel(
            @JsonProperty(value = "gameId") String gameId,
            @JsonProperty(value = "tableId") String tableId) {
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getTableId() {
        return tableId;
    }

}
