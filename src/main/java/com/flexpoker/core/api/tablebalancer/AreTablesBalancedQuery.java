package com.flexpoker.core.api.tablebalancer;

import com.flexpoker.model.Game;

public interface AreTablesBalancedQuery {

    boolean execute(Game game);

}
