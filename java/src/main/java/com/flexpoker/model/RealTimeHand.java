package com.flexpoker.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to store some real-time information about the hand that is currently
 * being played on a specific table.
 *
 * TODO: This would be the proper place to add split pots, who is in which ones,
 *       and how many chips are in each.
 *
 * @author cwoolner
 */
public class RealTimeHand {

    private Map<Seat, Set<GameEventType>> possibleSeatActionsMap =
            new HashMap<Seat, Set<GameEventType>>();

    private Map<Seat, Integer> amountNeededToCall = new HashMap<Seat, Integer>();

    private Map<Seat, Integer> amountNeededToRaise = new HashMap<Seat, Integer>();

    private Seat originatingBettor;

    private boolean roundComplete;

    private boolean handComplete;

    private boolean flopDealt;

    private boolean turnDealt;

    private boolean riverDealt;

    private Seat lastToAct;

    private Seat nextToAct;

    public RealTimeHand(Set<Seat> seats) {
        for (Seat seat : seats) {
            possibleSeatActionsMap.put(seat, new HashSet<GameEventType>());
            amountNeededToCall.put(seat, null);
            amountNeededToRaise.put(seat, null);
        }
    }

    public void addPossibleSeatAction(Seat Seat, GameEventType action) {
        possibleSeatActionsMap.get(Seat).add(action);
    }

    public void removePossibleSeatAction(Seat Seat, String action) {
        possibleSeatActionsMap.get(Seat).remove(action);
    }

    public Integer getAmountNeededToCall(Seat Seat) {
        return amountNeededToCall.get(Seat);
    }

    public void setAmountNeededToCall(Seat seat, Integer amount) {
        amountNeededToCall.put(seat, amount);
    }

    public Integer getAmountNeededToRaise(Seat Seat) {
        return amountNeededToRaise.get(Seat);
    }

    public void setAmountNeededToRaise(Seat Seat, Integer amount) {
        amountNeededToRaise.put(Seat, amount);
    }

    public Seat getOriginatingBettor() {
        return originatingBettor;
    }

    public void setOriginatingBettor(Seat originatingBettor) {
        this.originatingBettor = originatingBettor;
    }

    public boolean isRoundComplete() {
        return roundComplete;
    }

    public void setRoundComplete(boolean roundComplete) {
        this.roundComplete = roundComplete;
    }

    public boolean isHandComplete() {
        return handComplete;
    }

    public void setHandComplete(boolean handComplete) {
        this.handComplete = handComplete;
    }

    public boolean isUserAllowedToPerformAction(GameEventType action,
            Seat seat) {
        return possibleSeatActionsMap.get(seat).contains(action);
    }

    public boolean isFlopDealt() {
        return flopDealt;
    }

    public void setFlopDealt(boolean flopDealt) {
        this.flopDealt = flopDealt;
    }

    public boolean isTurnDealt() {
        return turnDealt;
    }

    public void setTurnDealt(boolean turnDealt) {
        this.turnDealt = turnDealt;
    }

    public boolean isRiverDealt() {
        return riverDealt;
    }

    public void setRiverDealt(boolean riverDealt) {
        this.riverDealt = riverDealt;
    }

    public Seat getLastToAct() {
        return lastToAct;
    }

    public void setLastToAct(Seat lastToAct) {
        this.lastToAct = lastToAct;
    }

    public Seat getNextToAct() {
        return nextToAct;
    }

    public void setNextToAct(Seat nextToAct) {
        this.nextToAct = nextToAct;
    }

}
