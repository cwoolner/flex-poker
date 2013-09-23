package com.flexpoker.core.api.tablebalancer;

import java.util.UUID;

public interface AssignInitialTablesForNewGame {

    void execute(UUID gameId);

}
