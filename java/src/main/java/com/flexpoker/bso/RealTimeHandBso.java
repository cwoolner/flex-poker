package com.flexpoker.bso;

import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Table;

public interface RealTimeHandBso {

    RealTimeHand get(Object table);

    RealTimeHand put(Table table, RealTimeHand realTimeHand);

}
