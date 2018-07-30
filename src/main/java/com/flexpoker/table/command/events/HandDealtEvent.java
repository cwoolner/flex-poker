package com.flexpoker.table.command.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.aggregate.HandDealerState;
import com.flexpoker.table.command.aggregate.HandEvaluation;
import com.flexpoker.table.command.framework.TableEvent;

public class HandDealtEvent extends BaseEvent implements TableEvent {

    private final UUID gameId;

    private final UUID handId;

    private final FlopCards flopCards;

    private final TurnCard turnCard;

    private final RiverCard riverCard;

    private final int buttonOnPosition;

    private final int smallBlindPosition;

    private final int bigBlindPosition;

    private final UUID lastToActPlayerId;

    private final Map<Integer, UUID> seatMap;

    private final Map<UUID, PocketCards> playerToPocketCardsMap;

    private final Map<UUID, Set<PlayerAction>> possibleSeatActionsMap;

    private final Set<UUID> playersStillInHand;

    private final List<HandEvaluation> handEvaluations;

    private final HandDealerState handDealerState;

    private final Map<UUID, Integer> chipsInBack;

    private final Map<UUID, Integer> chipsInFrontMap;

    private final Map<UUID, Integer> callAmountsMap;

    private final Map<UUID, Integer> raiseToAmountsMap;

    private final int smallBlind;

    private final int bigBlind;

    public HandDealtEvent(UUID aggregateId, UUID gameId, UUID handId, FlopCards flopCards, TurnCard turnCard,
            RiverCard riverCard, int buttonOnPosition, int smallBlindPosition, int bigBlindPosition,
            UUID lastToActPlayerId, Map<Integer, UUID> seatMap, Map<UUID, PocketCards> playerToPocketCardsMap,
            Map<UUID, Set<PlayerAction>> possibleSeatActionsMap, Set<UUID> playersStillInHand,
            List<HandEvaluation> handEvaluations, HandDealerState handDealerState, Map<UUID, Integer> chipsInBack,
            Map<UUID, Integer> chipsInFrontMap, Map<UUID, Integer> callAmountsMap, Map<UUID, Integer> raiseToAmountsMap,
            int smallBlind, int bigBlind) {
        super(aggregateId);
        this.gameId = gameId;
        this.handId = handId;
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.buttonOnPosition = buttonOnPosition;
        this.smallBlindPosition = smallBlindPosition;
        this.bigBlindPosition = bigBlindPosition;
        this.lastToActPlayerId = lastToActPlayerId;
        this.seatMap = new HashMap<>(seatMap);
        this.playerToPocketCardsMap = new HashMap<>(playerToPocketCardsMap);
        this.possibleSeatActionsMap = new HashMap<>(possibleSeatActionsMap);
        this.playersStillInHand = new HashSet<>(playersStillInHand);
        this.handEvaluations = new ArrayList<>(handEvaluations);
        this.handDealerState = handDealerState;
        this.chipsInBack = new HashMap<>(chipsInBack);
        this.chipsInFrontMap = new HashMap<>(chipsInFrontMap);
        this.callAmountsMap = new HashMap<>(callAmountsMap);
        this.raiseToAmountsMap = new HashMap<>(raiseToAmountsMap);
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    @JsonProperty
    @Override
    public UUID getGameId() {
        return gameId;
    }

    @JsonProperty
    public UUID getHandId() {
        return handId;
    }

    @JsonProperty
    public FlopCards getFlopCards() {
        return flopCards;
    }

    @JsonProperty
    public TurnCard getTurnCard() {
        return turnCard;
    }

    @JsonProperty
    public RiverCard getRiverCard() {
        return riverCard;
    }

    @JsonProperty
    public int getButtonOnPosition() {
        return buttonOnPosition;
    }

    @JsonProperty
    public int getSmallBlindPosition() {
        return smallBlindPosition;
    }

    @JsonProperty
    public int getBigBlindPosition() {
        return bigBlindPosition;
    }

    @JsonProperty
    public UUID getLastToActPlayerId() {
        return lastToActPlayerId;
    }

    @JsonProperty
    public Map<Integer, UUID> getSeatMap() {
        return new HashMap<>(seatMap);
    }

    @JsonProperty
    public Map<UUID, PocketCards> getPlayerToPocketCardsMap() {
        return new HashMap<>(playerToPocketCardsMap);
    }

    @JsonProperty
    public Map<UUID, Set<PlayerAction>> getPossibleSeatActionsMap() {
        return new HashMap<>(possibleSeatActionsMap);
    }

    @JsonProperty
    public Set<UUID> getPlayersStillInHand() {
        return new HashSet<>(playersStillInHand);
    }

    @JsonProperty
    public List<HandEvaluation> getHandEvaluations() {
        return new ArrayList<>(handEvaluations);
    }

    @JsonProperty
    public HandDealerState getHandDealerState() {
        return handDealerState;
    }

    @JsonProperty
    public Map<UUID, Integer> getChipsInBack() {
        return new HashMap<>(chipsInBack);
    }

    @JsonProperty
    public Map<UUID, Integer> getChipsInFrontMap() {
        return new HashMap<>(chipsInFrontMap);
    }

    @JsonProperty
    public Map<UUID, Integer> getCallAmountsMap() {
        return new HashMap<>(callAmountsMap);
    }

    @JsonProperty
    public Map<UUID, Integer> getRaiseToAmountsMap() {
        return new HashMap<>(raiseToAmountsMap);
    }

    @JsonProperty
    public int getSmallBlind() {
        return smallBlind;
    }

    @JsonProperty
    public int getBigBlind() {
        return bigBlind;
    }

}
