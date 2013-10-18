package com.flexpoker.core.api.pot;

import java.util.Set;

import com.flexpoker.model.Pot;
import com.flexpoker.model.Table;

public interface CalculatePotsAfterRoundQuery {

    Set<Pot> execute(Table table);

}
