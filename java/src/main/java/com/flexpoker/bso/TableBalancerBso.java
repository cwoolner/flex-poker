package com.flexpoker.bso;

import java.util.List;
import java.util.Set;

import com.flexpoker.model.Table;
import com.flexpoker.model.TableMovement;

public interface TableBalancerBso {

    boolean areTablesBalanced(List<Table> tables);

    Set<TableMovement> calculateTableMovements(List<Table> tables);

}
