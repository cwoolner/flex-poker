package com.flexpoker.web.dto.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeatDTO {

    private final int position;

    private final String name;

    private final int chipsInBack;

    private final int chipsInFront;

    private final boolean stillInHand;

    private final int raiseTo;

    private final int callAmount;

    private final boolean button;

    private final boolean smallBlind;

    private final boolean bigBlind;

    private final boolean actionOn;

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

    @JsonProperty
    public int getPosition() {
        return position;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public int getChipsInBack() {
        return chipsInBack;
    }

    @JsonProperty
    public int getChipsInFront() {
        return chipsInFront;
    }

    @JsonProperty
    public boolean isStillInHand() {
        return stillInHand;
    }

    @JsonProperty
    public int getRaiseTo() {
        return raiseTo;
    }

    @JsonProperty
    public int getCallAmount() {
        return callAmount;
    }

    @JsonProperty
    public boolean isButton() {
        return button;
    }

    @JsonProperty
    public boolean isSmallBlind() {
        return smallBlind;
    }

    @JsonProperty
    public boolean isBigBlind() {
        return bigBlind;
    }

    @JsonProperty
    public boolean isActionOn() {
        return actionOn;
    }

    public static SeatDTO createForNewTable(int position, String displayName, int startingNumberOfChips) {
        return new SeatDTO(position, displayName, startingNumberOfChips, 0, false, 0, 0, false, false, false, false);
    }

}
