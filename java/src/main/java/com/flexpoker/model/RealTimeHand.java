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

    private Map<Seat, Set<String>> possibleSeatActionsMap =
            new HashMap<Seat, Set<String>>();

    private Map<Seat, Integer> amountNeededToCall = new HashMap<Seat, Integer>();

    private Map<Seat, Integer> amountNeededToRaise = new HashMap<Seat, Integer>();

    private Seat originatingBettor;

    private boolean roundComplete;

    private boolean handComplete;

    private boolean flopDealt;

    private boolean turnDealt;

    private boolean riverDealt;

    public RealTimeHand(Set<Seat> seats) {
        for (Seat seat : seats) {
            possibleSeatActionsMap.put(seat, new HashSet<String>());
            amountNeededToCall.put(seat, null);
            amountNeededToRaise.put(seat, null);
        }
    }

    public void addPossibleSeatAction(Seat Seat, String action) {
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

    public boolean isUserAllowedToPerformAction(String action, Seat seat) {
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
        // TODO Auto-generated method stub
        return null;
    }

    public Seat getNextToAct() {
        // TODO Auto-generated method stub
        return null;
    }

}
