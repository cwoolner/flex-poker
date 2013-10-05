package com.flexpoker.web.model.table;

public class SeatViewModel {

    private int position;
    
    private String name;
    
    private int chipsInBack;
    
    private int chipsInFront;
    
    private boolean stillInHand;

    private int raiseTo;

    private int callAmount;

    private boolean button;

    private boolean smallBlind;

    private boolean bigBlind;

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

}
