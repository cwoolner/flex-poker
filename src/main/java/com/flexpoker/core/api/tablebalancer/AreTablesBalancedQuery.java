package com.flexpoker.core.api.tablebalancer;

import java.util.UUID;

public interface AreTablesBalancedQuery {

    boolean execute(UUID gameId);

}
