package com.flexpoker.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
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
public class Hand {

    private Map<Seat, Set<GameEventType>> possibleSeatActionsMap =
            new HashMap<Seat, Set<GameEventType>>();

    private Seat originatingBettor;

    private Seat lastToAct;

    private Seat nextToAct;

    private HandDealerState handDealerState = HandDealerState.NONE;

    private HandRoundState handRoundState = HandRoundState.ROUND_COMPLETE;

    private List<HandEvaluation> handEvaluationList;
    
    private int totalPotAmount;
    
    private Set<Pot> pots;
    
    public Hand(List<Seat> seats) {
        for (Seat seat : seats) {
            possibleSeatActionsMap.put(seat, new HashSet<GameEventType>());
        }
    }

    public void addPossibleSeatAction(Seat Seat, GameEventType action) {
        possibleSeatActionsMap.get(Seat).add(action);
    }

    public void removePossibleSeatAction(Seat Seat, GameEventType action) {
        possibleSeatActionsMap.get(Seat).remove(action);
    }

    public Seat getOriginatingBettor() {
        return originatingBettor;
    }

    public void setOriginatingBettor(Seat originatingBettor) {
        this.originatingBettor = originatingBettor;
    }

    public boolean isUserAllowedToPerformAction(GameEventType action, Seat seat) {
        return possibleSeatActionsMap.get(seat).contains(action);
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

    public HandDealerState getHandDealerState() {
        return handDealerState;
    }

    public void setHandDealerState(HandDealerState handDealerState) {
        this.handDealerState = handDealerState;
    }

    public HandRoundState getHandRoundState() {
        return handRoundState;
    }

    public void setHandRoundState(HandRoundState handRoundState) {
        this.handRoundState = handRoundState;
    }

    public List<HandEvaluation> getHandEvaluationList() {
        return handEvaluationList;
    }

    public void setHandEvaluationList(List<HandEvaluation> handEvaluationList) {
        this.handEvaluationList = handEvaluationList;
    }

    public Set<Pot> getPots() {
        return pots;
    }

    public void setPots(Set<Pot> pots) {
        this.pots = pots;
    }

    public void addToTotalPot(int chipsInFront) {
        totalPotAmount += chipsInFront;
    }

    public int getTotalPotAmount() {
        return totalPotAmount;
    }

}
