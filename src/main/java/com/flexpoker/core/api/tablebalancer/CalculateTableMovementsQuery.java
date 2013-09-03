package com.flexpoker.core.api.tablebalancer;

import java.util.List;

import com.flexpoker.model.TableMovement;

public interface CalculateTableMovementsQuery {

    List<TableMovement> execute(Integer gameId);

}
