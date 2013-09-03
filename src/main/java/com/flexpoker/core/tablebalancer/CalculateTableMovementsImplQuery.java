package com.flexpoker.core.tablebalancer;

import java.util.List;

import com.flexpoker.config.Query;
import com.flexpoker.core.api.tablebalancer.CalculateTableMovementsQuery;
import com.flexpoker.model.TableMovement;

@Query
public class CalculateTableMovementsImplQuery implements CalculateTableMovementsQuery {

    @Override
    public List<TableMovement> execute(Integer gameId) {
        // TODO Auto-generated method stub
        return null;
    }

}
