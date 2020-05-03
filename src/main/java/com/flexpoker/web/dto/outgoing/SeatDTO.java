package com.flexpoker.web.dto.outgoing;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public SeatDTO(
            @JsonProperty(value = "position") int position,
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "chipsInBack") int chipsInBack,
            @JsonProperty(value = "chipsInFront") int chipsInFront,
            @JsonProperty(value = "stillInHand") boolean stillInHand,
            @JsonProperty(value = "raiseTo") int raiseTo,
            @JsonProperty(value = "callAmount") int callAmount,
            @JsonProperty(value = "button") boolean button,
            @JsonProperty(value = "smallBlind") boolean smallBlind,
            @JsonProperty(value = "bigBlind") boolean bigBlind,
            @JsonProperty(value = "actionOn") boolean actionOn) {
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

    public String getName() {
        return name;
    }

    public int getChipsInBack() {
        return chipsInBack;
    }

    public int getChipsInFront() {
        return chipsInFront;
    }

    public boolean isStillInHand() {
        return stillInHand;
    }

    public int getRaiseTo() {
        return raiseTo;
    }

    public int getCallAmount() {
        return callAmount;
    }

    public boolean isButton() {
        return button;
    }

    public boolean isSmallBlind() {
        return smallBlind;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public boolean isActionOn() {
        return actionOn;
    }

    public static SeatDTO createForNewTable(int position, String displayName, int startingNumberOfChips) {
        return new SeatDTO(position, displayName, startingNumberOfChips, 0, false, 0, 0, false, false, false, false);
    }

}
