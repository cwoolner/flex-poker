package com.flexpoker.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;

import com.flexpoker.model.card.Deck;

public class Hand {

    private Map<Seat, Set<GameEventType>> possibleSeatActionsMap;

    private Seat originatingBettor;

    private Seat lastToAct;

    private Seat nextToAct;

    private HandDealerState handDealerState;

    private HandRoundState handRoundState;

    private List<HandEvaluation> handEvaluationList;
    
    private int totalPotAmount;
    
    private Set<Pot> pots;
    
    private final Deck deck;

    public Hand(List<Seat> seats, Deck deck) {
        possibleSeatActionsMap = new HashMap<>();
        for (Seat seat : seats) {
            possibleSeatActionsMap.put(seat, new HashSet<GameEventType>());
        }
        
        this.deck = deck;
        this.handDealerState = HandDealerState.NONE;
        this.handRoundState = HandRoundState.ROUND_COMPLETE;
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
        Collections.sort(handEvaluationList);
        Collections.reverse(handEvaluationList);
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

    public Deck getDeck() {
        return deck;
    }
    
    public void resetPlayerActions(Seat seat) {
        possibleSeatActionsMap.get(seat).remove(GameEventType.CHECK);
        possibleSeatActionsMap.get(seat).remove(GameEventType.RAISE);
        possibleSeatActionsMap.get(seat).remove(GameEventType.CALL);
        possibleSeatActionsMap.get(seat).remove(GameEventType.FOLD);
    }
    
    public void moveToNextDealerState() {
        handDealerState = HandDealerState.values()[handDealerState.ordinal() + 1];
    }

}
