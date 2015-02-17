package com.flexpoker.web.model.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeatDTO {

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

    public SeatDTO(int position, String name, int chipsInBack, int chipsInFront,
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChipsInBack() {
        return chipsInBack;
    }

    public void setChipsInBack(int chipsInBack) {
        this.chipsInBack = chipsInBack;
    }

    public int getChipsInFront() {
        return chipsInFront;
    }

    public void setChipsInFront(int chipsInFront) {
        this.chipsInFront = chipsInFront;
    }

    public boolean isStillInHand() {
        return stillInHand;
    }

    public void setStillInHand(boolean stillInHand) {
        this.stillInHand = stillInHand;
    }

    public int getRaiseTo() {
        return raiseTo;
    }

    public void setRaiseTo(int raiseTo) {
        this.raiseTo = raiseTo;
    }

    public int getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(int callAmount) {
        this.callAmount = callAmount;
    }

    public boolean isButton() {
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }

    public boolean isSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(boolean smallBlind) {
        this.smallBlind = smallBlind;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(boolean bigBlind) {
        this.bigBlind = bigBlind;
    }

    public boolean isActionOn() {
        return actionOn;
    }

    public void setActionOn(boolean actionOn) {
        this.actionOn = actionOn;
    }

}
