package com.flexpoker.table.command.aggregate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRoundState;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.Pot;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.events.HandDealtEvent;

public class Hand {

    private final Map<Integer, UUID> seatMap;

    private final UUID entityId;

    private final FlopCards flopCards;

    private final TurnCard turnCard;

    private final RiverCard riverCard;

    private final int buttonOnPosition;

    private final int smallBlindPosition;

    private final int bigBlindPosition;

    private int actionOnPosition;

    private final Map<UUID, PocketCards> playerToPocketCardsMap;

    private final Map<UUID, Set<PlayerAction>> possibleSeatActionsMap;

    private UUID originatingBettorPlayerId;

    private UUID lastToActPlayerId;

    private UUID nextToActPlayerId;

    private HandDealerState handDealerState;

    private HandRoundState handRoundState;

    private final List<HandEvaluation> handEvaluationList;

    private final Set<Pot> pots;

    private final Set<UUID> playersStillInHand;

    private Timer actionOnTimer;

    private final Map<UUID, Integer> chipsInBackMap;

    private final Map<UUID, Integer> chipsInFrontMap;

    private final Map<UUID, Integer> callAmountsMap;

    private final Map<UUID, Integer> raiseToAmountsMap;

    private final Blinds blinds;

    public Hand(UUID entityId, Map<Integer, UUID> seatMap, FlopCards flopCards,
            TurnCard turnCard, RiverCard riverCard, int buttonOnPosition,
            int smallBlindPosition, int bigBlindPosition, int actionOnPosition,
            Map<UUID, PocketCards> playerToPocketCardsMap,
            Map<UUID, Set<PlayerAction>> possibleSeatActionsMap,
            Set<UUID> playersStillInHand, List<HandEvaluation> handEvaluationList,
            Set<Pot> pots, HandDealerState handDealerState,
            HandRoundState handRoundState, Map<UUID, Integer> chipsInBack,
            Map<UUID, Integer> chipsInFrontMap, Map<UUID, Integer> callAmountsMap,
            Map<UUID, Integer> raiseToAmountsMap, Blinds blinds) {
        this.entityId = entityId;
        this.seatMap = seatMap;
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.buttonOnPosition = buttonOnPosition;
        this.smallBlindPosition = smallBlindPosition;
        this.bigBlindPosition = bigBlindPosition;
        this.actionOnPosition = actionOnPosition;
        this.playerToPocketCardsMap = playerToPocketCardsMap;
        this.handEvaluationList = handEvaluationList;
        this.possibleSeatActionsMap = possibleSeatActionsMap;
        this.playersStillInHand = playersStillInHand;
        this.pots = pots;
        this.handDealerState = handDealerState;
        this.handRoundState = handRoundState;
        this.chipsInBackMap = chipsInBack;
        this.chipsInFrontMap = chipsInFrontMap;
        this.callAmountsMap = callAmountsMap;
        this.raiseToAmountsMap = raiseToAmountsMap;
        this.blinds = blinds;
    }

    public HandDealtEvent dealHand(UUID aggregateId, int aggregateVersion, UUID gameId) {
        for (Integer seatPosition : seatMap.keySet()) {
            UUID playerId = seatMap.get(seatPosition);

            if (playerId == null) {
                continue;
            }

            int chipsInFront = 0;
            int callAmount = blinds.getBigBlind();
            int raiseToAmount = blinds.getBigBlind() * 2;

            if (seatPosition == bigBlindPosition) {
                chipsInFront = blinds.getBigBlind();
                callAmount = 0;
            } else if (seatPosition == smallBlindPosition) {
                chipsInFront = blinds.getSmallBlind();
                callAmount = blinds.getSmallBlind();
            }

            if (chipsInFront > chipsInBackMap.get(playerId)) {
                chipsInFrontMap.put(playerId, chipsInBackMap.get(playerId));
            } else {
                chipsInFrontMap.put(playerId, chipsInFront);
            }

            chipsInBackMap.put(playerId,
                    chipsInBackMap.get(playerId) - chipsInFrontMap.get(playerId));

            if (callAmount > chipsInBackMap.get(playerId)) {
                callAmountsMap.put(playerId, chipsInBackMap.get(playerId));
            } else {
                callAmountsMap.put(playerId, callAmount);
            }

            int totalChips = chipsInBackMap.get(playerId) + chipsInFrontMap.get(playerId);

            if (raiseToAmount > totalChips) {
                raiseToAmountsMap.put(playerId, totalChips);
            } else {
                raiseToAmountsMap.put(playerId, raiseToAmount);
            }

            if (raiseToAmountsMap.get(playerId) > 0) {
                Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
                playerActions.add(PlayerAction.RAISE);
            }

            if (callAmountsMap.get(playerId) > 0) {
                Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
                playerActions.add(PlayerAction.CALL);
                playerActions.add(PlayerAction.FOLD);
            } else {
                Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
                playerActions.add(PlayerAction.CHECK);
            }
        }

        lastToActPlayerId = seatMap.get(Integer.valueOf(bigBlindPosition));
        handDealerState = HandDealerState.POCKET_CARDS_DEALT;
        handRoundState = HandRoundState.ROUND_IN_PROGRESS;

        // TODO: set action on timer

        HandDealtEvent handDealtEvent = new HandDealtEvent(aggregateId, aggregateVersion,
                gameId, entityId, flopCards, turnCard, riverCard, buttonOnPosition,
                smallBlindPosition, bigBlindPosition, actionOnPosition, seatMap,
                playerToPocketCardsMap, possibleSeatActionsMap, playersStillInHand,
                handEvaluationList, pots, handDealerState, handRoundState,
                chipsInBackMap, chipsInFrontMap, callAmountsMap, raiseToAmountsMap,
                blinds);
        return handDealtEvent;
    }
}
