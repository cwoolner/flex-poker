package com.flexpoker.bso;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.flexpoker.model.RealTimeHand;
import com.flexpoker.model.Table;

@Service("realTimeHandBso")
public class RealTimeHandBsoImpl extends HashMap<Table, RealTimeHand>
        implements RealTimeHandBso {

    private static final long serialVersionUID = 8388245872956508429L;

}
