package com.flexpoker.table.command.events;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;

public class HandDealtEvent extends BaseEvent<TableEventType> implements TableEvent {

    private static final TableEventType TYPE = TableEventType.HandDealtEvent;

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

    private final Blinds blinds;

    private final Set<UUID> playersToShowCardsMap;

    @JsonCreator
    public HandDealtEvent(
            @JsonProperty(value = "aggregateId") UUID aggregateId,
            @JsonProperty(value = "version") int version,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "handId") UUID handId,
            @JsonProperty(value = "flopCards") FlopCards flopCards,
            @JsonProperty(value = "turnCard") TurnCard turnCard,
            @JsonProperty(value = "riverCard") RiverCard riverCard,
            @JsonProperty(value = "buttonOnPosition") int buttonOnPosition,
            @JsonProperty(value = "smallBlindPosition") int smallBlindPosition,
            @JsonProperty(value = "bigBlindPosition") int bigBlindPosition,
            @JsonProperty(value = "lastToActPlayerId") UUID lastToActPlayerId,
            @JsonProperty(value = "seatMap") Map<Integer, UUID> seatMap,
            @JsonProperty(value = "playerToPocketCardsMap") Map<UUID, PocketCards> playerToPocketCardsMap,
            @JsonProperty(value = "possibleSeatActionsMap") Map<UUID, Set<PlayerAction>> possibleSeatActionsMap,
            @JsonProperty(value = "playersStillInHand") Set<UUID> playersStillInHand,
            @JsonProperty(value = "handEvaluations") List<HandEvaluation> handEvaluations,
            @JsonProperty(value = "handDealerState") HandDealerState handDealerState,
            @JsonProperty(value = "chipsInBack") Map<UUID, Integer> chipsInBack,
            @JsonProperty(value = "chipsInFrontMap") Map<UUID, Integer> chipsInFrontMap,
            @JsonProperty(value = "callAmountsMap") Map<UUID, Integer> callAmountsMap,
            @JsonProperty(value = "raiseToAmountsMap") Map<UUID, Integer> raiseToAmountsMap,
            @JsonProperty(value = "blinds") Blinds blinds,
            @JsonProperty(value = "playersToShowCardsMap") Set<UUID> playersToShowCardsMap) {
        super(aggregateId, version, TYPE);
        this.gameId = gameId;
        this.handId = handId;
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.buttonOnPosition = buttonOnPosition;
        this.smallBlindPosition = smallBlindPosition;
        this.bigBlindPosition = bigBlindPosition;
        this.lastToActPlayerId = lastToActPlayerId;
        this.seatMap = seatMap;
        this.playerToPocketCardsMap = playerToPocketCardsMap;
        this.possibleSeatActionsMap = possibleSeatActionsMap;
        this.playersStillInHand = playersStillInHand;
        this.handEvaluations = handEvaluations;
        this.handDealerState = handDealerState;
        this.chipsInBack = chipsInBack;
        this.chipsInFrontMap = chipsInFrontMap;
        this.callAmountsMap = callAmountsMap;
        this.raiseToAmountsMap = raiseToAmountsMap;
        this.blinds = blinds;
        this.playersToShowCardsMap = playersToShowCardsMap;
    }

    @Override
    public UUID getGameId() {
        return gameId;
    }

    public UUID getHandId() {
        return handId;
    }

    public FlopCards getFlopCards() {
        return flopCards;
    }

    public TurnCard getTurnCard() {
        return turnCard;
    }

    public RiverCard getRiverCard() {
        return riverCard;
    }

    public int getButtonOnPosition() {
        return buttonOnPosition;
    }

    public int getSmallBlindPosition() {
        return smallBlindPosition;
    }

    public int getBigBlindPosition() {
        return bigBlindPosition;
    }

    public UUID getLastToActPlayerId() {
        return lastToActPlayerId;
    }

    public Map<Integer, UUID> getSeatMap() {
        return seatMap;
    }

    public Map<UUID, PocketCards> getPlayerToPocketCardsMap() {
        return playerToPocketCardsMap;
    }

    public Map<UUID, Set<PlayerAction>> getPossibleSeatActionsMap() {
        return possibleSeatActionsMap;
    }

    public Set<UUID> getPlayersStillInHand() {
        return playersStillInHand;
    }

    public List<HandEvaluation> getHandEvaluations() {
        return handEvaluations;
    }

    public HandDealerState getHandDealerState() {
        return handDealerState;
    }

    public Map<UUID, Integer> getChipsInBack() {
        return chipsInBack;
    }

    public Map<UUID, Integer> getChipsInFrontMap() {
        return chipsInFrontMap;
    }

    public Map<UUID, Integer> getCallAmountsMap() {
        return callAmountsMap;
    }

    public Map<UUID, Integer> getRaiseToAmountsMap() {
        return raiseToAmountsMap;
    }

    public Blinds getBlinds() {
        return blinds;
    }

    public Set<UUID> getPlayersToShowCardsMap() {
        return playersToShowCardsMap;
    }

}
