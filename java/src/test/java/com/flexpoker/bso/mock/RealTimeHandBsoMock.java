package com.flexpoker.bso.mock;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.RealTimeHandBso;
import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Table;

@Service
public class RealTimeHandBsoMock implements RealTimeHandBso {

    @Override
    public RealTimeHand get(Object table) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RealTimeHand put(Table table, RealTimeHand realTimeHand) {
        // TODO Auto-generated method stub
        return null;
    }

}
