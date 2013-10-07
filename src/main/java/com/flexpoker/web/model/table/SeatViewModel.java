package com.flexpoker.web.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeatViewModel {

    @JsonProperty
    private int position;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private int chipsInBack;
    
    @JsonProperty
    private int chipsInFront;
    
    @JsonProperty
    private boolean stillInHand;

    @JsonProperty
    private int raiseTo;

    @JsonProperty
    private int callAmount;

    @JsonProperty
    private boolean button;

    @JsonProperty
    private boolean smallBlind;

    @JsonProperty
    private boolean bigBlind;

    @JsonProperty
    private boolean actionOn;
    
    public SeatViewModel(int position, String name, int chipsInBack, int chipsInFront,
            boolean stillInHand, int raiseTo, int callAmount, boolean button,
            boolean smallBlind, boolean bigBlind, boolean actionOn) {
        this.position = position;
        this.name = name;
        this.chipsInBack = chipsInBack;
        this.chipsInFront = chipsInFront;
        this.stillInHand = stillInHand;
        this.raiseTo = raiseTo;
        this.callAmount = callAmount;
        this.button = button;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.actionOn = actionOn;
    }

}
