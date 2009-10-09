package com.flexpoker.model;

public class HandState {

    private final HandDealerState handDealerState;

    private final HandRoundState handRoundState;

    public HandState(HandDealerState handDealerState, HandRoundState handRoundState) {
        this.handDealerState = handDealerState;
        this.handRoundState = handRoundState;
    }

    public HandDealerState getHandDealerState() {
        return handDealerState;
    }

    public HandRoundState getHandRoundState() {
        return handRoundState;
    }

}