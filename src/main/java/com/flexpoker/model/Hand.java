package com.flexpoker.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.flexpoker.model.card.CardsUsedInHand;

public class Hand {

    private Map<Seat, Set<PlayerAction>> possibleSeatActionsMap;

    private Seat originatingBettor;

    private Seat lastToAct;

    private Seat nextToAct;

    private HandDealerState handDealerState;

    private List<HandEvaluation> handEvaluationList;

    private int totalPotAmount;

    private Set<Pot> pots;

    private final CardsUsedInHand deck;

    public Hand(List<Seat> seats, CardsUsedInHand deck) {
        possibleSeatActionsMap = new HashMap<>();
        for (Seat seat : seats) {
            possibleSeatActionsMap.put(seat, new HashSet<PlayerAction>());
        }

        this.deck = deck;
        this.handDealerState = HandDealerState.NONE;
        this.pots = new HashSet<>();
    }

    public void addPossibleSeatAction(Seat Seat, PlayerAction action) {
        possibleSeatActionsMap.get(Seat).add(action);
    }

    public void removePossibleSeatAction(Seat Seat, PlayerAction action) {
        possibleSeatActionsMap.get(Seat).remove(action);
    }

    public Seat getOriginatingBettor() {
        return originatingBettor;
    }

    public void setOriginatingBettor(Seat originatingBettor) {
        this.originatingBettor = originatingBettor;
    }

    public boolean isUserAllowedToPerformAction(PlayerAction action, Seat seat) {
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

    public CardsUsedInHand getDeck() {
        return deck;
    }

    public void resetPlayerActions(Seat seat) {
        possibleSeatActionsMap.get(seat).remove(PlayerAction.CHECK);
        possibleSeatActionsMap.get(seat).remove(PlayerAction.RAISE);
        possibleSeatActionsMap.get(seat).remove(PlayerAction.CALL);
        possibleSeatActionsMap.get(seat).remove(PlayerAction.FOLD);
    }

    public void moveToNextDealerState() {
        if (handDealerState != HandDealerState.COMPLETE) {
            handDealerState = HandDealerState.values()[handDealerState.ordinal() + 1];
        }
    }

    public void removeSeatFromPots(Seat seat) {
        for (Pot pot : pots) {
            // pot.removeSeat(seat);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
