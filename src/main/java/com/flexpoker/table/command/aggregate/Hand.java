package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.card.FlopCards;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.model.card.RiverCard;
import com.flexpoker.model.card.TurnCard;
import com.flexpoker.table.command.aggregate.pot.PotHandler;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerForceCheckedEvent;
import com.flexpoker.table.command.events.PlayerForceFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class Hand {

    private final UUID gameId;

    private final UUID tableId;

    private final UUID entityId;

    private final Map<Integer, UUID> seatMap;

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

    private HandDealerState handDealerState;

    private final List<HandEvaluation> handEvaluationList;

    private final Set<UUID> playersStillInHand;

    private final Map<UUID, Integer> chipsInBackMap;

    private final Map<UUID, Integer> chipsInFrontMap;

    private final Map<UUID, Integer> callAmountsMap;

    private final Map<UUID, Integer> raiseToAmountsMap;

    private final int smallBlind;

    private final int bigBlind;

    private final Set<UUID> playersToShowCards;

    private boolean flopDealt;

    private boolean turnDealt;

    private boolean riverDealt;

    private final PotHandler potHandler;

    public Hand(UUID gameId, UUID tableId, UUID entityId, Map<Integer, UUID> seatMap,
            FlopCards flopCards, TurnCard turnCard, RiverCard riverCard,
            int buttonOnPosition, int smallBlindPosition, int bigBlindPosition,
            UUID lastToActPlayerId, Map<UUID, PocketCards> playerToPocketCardsMap,
            Map<UUID, Set<PlayerAction>> possibleSeatActionsMap,
            Set<UUID> playersStillInHand, List<HandEvaluation> handEvaluationList,
            HandDealerState handDealerState, Map<UUID, Integer> chipsInBack,
            Map<UUID, Integer> chipsInFrontMap, Map<UUID, Integer> callAmountsMap,
            Map<UUID, Integer> raiseToAmountsMap, int smallBlind, int bigBlind) {
        this.gameId = gameId;
        this.tableId = tableId;
        this.entityId = entityId;
        this.seatMap = seatMap;
        this.flopCards = flopCards;
        this.turnCard = turnCard;
        this.riverCard = riverCard;
        this.buttonOnPosition = buttonOnPosition;
        this.smallBlindPosition = smallBlindPosition;
        this.bigBlindPosition = bigBlindPosition;
        this.lastToActPlayerId = lastToActPlayerId;
        this.playerToPocketCardsMap = playerToPocketCardsMap;
        this.handEvaluationList = handEvaluationList;
        this.possibleSeatActionsMap = possibleSeatActionsMap;
        this.playersStillInHand = playersStillInHand;
        this.handDealerState = handDealerState;
        this.chipsInBackMap = chipsInBack;
        this.chipsInFrontMap = chipsInFrontMap;
        this.callAmountsMap = callAmountsMap;
        this.raiseToAmountsMap = raiseToAmountsMap;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.playersToShowCards = new HashSet<>();
        this.potHandler = new PotHandler(gameId, tableId, entityId,
                handEvaluationList);
    }

    public List<TableEvent> dealHand(int aggregateVersion, int actionOnPosition) {
        List<TableEvent> eventsCreated = new ArrayList<>();

        seatMap.keySet().stream().filter(x -> seatMap.get(x) != null)
                .forEach(seatPosition -> {
                    handleStartOfHandPlayerValues(seatPosition.intValue());
                });

        lastToActPlayerId = seatMap.get(Integer.valueOf(bigBlindPosition));
        handDealerState = HandDealerState.POCKET_CARDS_DEALT;

        HandDealtEvent handDealtEvent = new HandDealtEvent(tableId, aggregateVersion,
                gameId, entityId, flopCards, turnCard, riverCard, buttonOnPosition,
                smallBlindPosition, bigBlindPosition, lastToActPlayerId, seatMap,
                playerToPocketCardsMap, possibleSeatActionsMap, playersStillInHand,
                handEvaluationList, handDealerState, chipsInBackMap, chipsInFrontMap,
                callAmountsMap, raiseToAmountsMap, smallBlind, bigBlind);
        eventsCreated.add(handDealtEvent);

        // creat an initial empty pot for the table
        eventsCreated.add(new PotCreatedEvent(tableId, aggregateVersion + 1,
                gameId, entityId, UUID.randomUUID(), playersStillInHand));

        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                aggregateVersion + 2, gameId, entityId, actionOnPlayerId);
        eventsCreated.add(actionOnChangedEvent);

        return eventsCreated;
    }

    private void handleStartOfHandPlayerValues(int seatPosition) {
        UUID playerId = seatMap.get(Integer.valueOf(seatPosition));

        int chipsInFront = 0;
        int callAmount = bigBlind;
        int raiseToAmount = bigBlind * 2;

        if (seatPosition == bigBlindPosition) {
            chipsInFront = bigBlind;
            callAmount = 0;
        } else if (seatPosition == smallBlindPosition) {
            chipsInFront = smallBlind;
            callAmount = smallBlind;
        }

        if (chipsInFront > chipsInBackMap.get(playerId).intValue()) {
            chipsInFrontMap.put(playerId, chipsInBackMap.get(playerId));
        } else {
            chipsInFrontMap.put(playerId, Integer.valueOf(chipsInFront));
        }

        subtractFromChipsInBack(playerId, chipsInFrontMap.get(playerId).intValue());

        if (callAmount > chipsInBackMap.get(playerId).intValue()) {
            callAmountsMap.put(playerId, chipsInBackMap.get(playerId));
        } else {
            callAmountsMap.put(playerId, Integer.valueOf(callAmount));
        }

        int totalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();

        if (raiseToAmount > totalChips) {
            raiseToAmountsMap.put(playerId, Integer.valueOf(totalChips));
        } else {
            raiseToAmountsMap.put(playerId, Integer.valueOf(raiseToAmount));
        }

        if (raiseToAmountsMap.get(playerId).intValue() > 0) {
            Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.RAISE);
        }

        if (callAmountsMap.get(playerId).intValue() > 0) {
            Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.CALL);
            playerActions.add(PlayerAction.FOLD);
        } else {
            Set<PlayerAction> playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.CHECK);
        }
    }

    public TableEvent check(UUID playerId, boolean forced, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CHECK);

        return forced
                ? new PlayerForceCheckedEvent(tableId, aggregateVersion, gameId, entityId, playerId)
                : new PlayerCheckedEvent(tableId, aggregateVersion, gameId, entityId, playerId);
    }

    public PlayerCalledEvent call(UUID playerId, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CALL);

        PlayerCalledEvent playerCalledEvent = new PlayerCalledEvent(tableId,
                aggregateVersion, gameId, entityId, playerId);
        return playerCalledEvent;
    }

    public TableEvent fold(UUID playerId, boolean forced, int aggregateVersion) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.FOLD);

        return forced
                ? new PlayerForceFoldedEvent(tableId, aggregateVersion, gameId, entityId, playerId)
                : new PlayerFoldedEvent(tableId, aggregateVersion, gameId, entityId, playerId);
    }

    public PlayerRaisedEvent raise(UUID playerId, int aggregateVersion, int raiseToAmount) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.RAISE);
        checkRaiseAmountValue(playerId, raiseToAmount);

        PlayerRaisedEvent playerRaisedEvent = new PlayerRaisedEvent(tableId,
                aggregateVersion, gameId, entityId, playerId, raiseToAmount);
        return playerRaisedEvent;
    }

    TableEvent expireActionOn(UUID playerId, int aggregateVersion) {
        if (callAmountsMap.get(playerId).intValue() == 0) {
            return check(playerId, true, aggregateVersion);
        }

        return fold(playerId, true, aggregateVersion);
    }

    public List<TableEvent> changeActionOn(int aggregateVersion) {
        List<TableEvent> eventsCreated = new ArrayList<>();

        // do not change action on if the hand is over. starting a new hand
        // should adjust that
        if (handDealerState == HandDealerState.COMPLETE) {
            return eventsCreated;
        }

        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        // the player just bet, so a new last to act needs to be determined
        if (actionOnPlayerId.equals(originatingBettorPlayerId)) {
            UUID nextPlayerToAct = findNextToAct();
            UUID lastPlayerToAct = seatMap.get(Integer.valueOf(determineLastToAct()));

            ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                    aggregateVersion, gameId, entityId, nextPlayerToAct);
            LastToActChangedEvent lastToActChangedEvent = new LastToActChangedEvent(
                    tableId, aggregateVersion + 1, gameId, entityId, lastPlayerToAct);

            eventsCreated.add(actionOnChangedEvent);
            eventsCreated.add(lastToActChangedEvent);

        }
        // if it's the last player to act, recalculate for a new round
        else if (actionOnPlayerId.equals(lastToActPlayerId)) {
            UUID nextPlayerToAct = findActionOnPlayerIdForNewRound();
            UUID newRoundLastPlayerToAct = seatMap.get(Integer
                    .valueOf(determineLastToAct()));

            ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                    aggregateVersion, gameId, entityId, nextPlayerToAct);
            LastToActChangedEvent lastToActChangedEvent = new LastToActChangedEvent(
                    tableId, aggregateVersion + 1, gameId, entityId,
                    newRoundLastPlayerToAct);
            eventsCreated.add(actionOnChangedEvent);
            eventsCreated.add(lastToActChangedEvent);
        }
        // just a normal transition mid-round
        else {
            UUID nextPlayerToAct = findNextToAct();
            ActionOnChangedEvent actionOnChangedEvent = new ActionOnChangedEvent(tableId,
                    aggregateVersion, gameId, entityId, nextPlayerToAct);
            eventsCreated.add(actionOnChangedEvent);
        }

        return eventsCreated;
    }

    Optional<TableEvent> dealCommonCardsIfAppropriate(int aggregateVersion) {
        if (handDealerState == HandDealerState.FLOP_DEALT && !flopDealt) {
            return Optional.of(new FlopCardsDealtEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        if (handDealerState == HandDealerState.TURN_DEALT && !turnDealt) {
            return Optional.of(new TurnCardDealtEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        if (handDealerState == HandDealerState.RIVER_DEALT && !riverDealt) {
            return Optional.of(new RiverCardDealtEvent(tableId, aggregateVersion, gameId,
                    entityId));
        }

        return Optional.empty();
    }

    List<TableEvent> handlePotAndRoundCompleted(int aggregateVersion) {
        if (!seatMap.get(Integer.valueOf(actionOnPosition)).equals(lastToActPlayerId)
                && playersStillInHand.size() > 1) {
            return Collections.emptyList();
        }

        List<TableEvent> tableEvents = new ArrayList<>();
        tableEvents.addAll(potHandler.calculatePots(aggregateVersion,
                chipsInFrontMap, chipsInBackMap, playersStillInHand));

        HandDealerState nextHandDealerState = playersStillInHand.size() == 1 ? HandDealerState.COMPLETE
                : HandDealerState.values()[handDealerState.ordinal() + 1];

        RoundCompletedEvent roundCompletedEvent = new RoundCompletedEvent(tableId, aggregateVersion
                + tableEvents.size(), gameId, entityId, nextHandDealerState);
        tableEvents.add(roundCompletedEvent);
        applyEvent(roundCompletedEvent);

        return tableEvents;
    }

    Optional<TableEvent> finishHandIfAppropriate(int aggregateVersion) {
        if (handDealerState == HandDealerState.COMPLETE) {
            return Optional.of(new HandCompletedEvent(tableId, aggregateVersion,
                    gameId, entityId, chipsInBackMap));
        }

        return Optional.empty();
    }

    private void checkRaiseAmountValue(UUID playerId, int raiseToAmount) {
        int playersTotalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();
        if (raiseToAmount < raiseToAmountsMap.get(playerId).intValue()
                || raiseToAmount > playersTotalChips) {
            throw new IllegalArgumentException("Raise amount must be between the "
                    + "minimum and maximum values.");
        }
    }

    private void checkActionOnPlayer(UUID playerId) {
        UUID actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));
        if (!playerId.equals(actionOnPlayerId)) {
            throw new FlexPokerException("action is not on the player attempting action");
        }
    }

    private void checkPerformAction(UUID playerId, PlayerAction playerAction) {
        if (!possibleSeatActionsMap.get(playerId).contains(playerAction)) {
            throw new FlexPokerException("not allowed to " + playerAction);
        }
    }

    void applyEvent(PlayerCheckedEvent event) {
        UUID playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerForceCheckedEvent event) {
        UUID playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerCalledEvent event) {
        UUID playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));

        int newChipsInFront = chipsInFrontMap.get(playerId).intValue()
                + callAmountsMap.get(playerId).intValue();
        chipsInFrontMap.put(playerId, Integer.valueOf(newChipsInFront));

        int newChipsInBack = chipsInBackMap.get(playerId).intValue()
                - callAmountsMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(newChipsInBack));
    }

    void applyEvent(PlayerFoldedEvent event) {
        UUID playerId = event.getPlayerId();
        playersStillInHand.remove(playerId);
        potHandler.removePlayerFromAllPots(playerId);
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerForceFoldedEvent event) {
        UUID playerId = event.getPlayerId();
        playersStillInHand.remove(playerId);
        potHandler.removePlayerFromAllPots(playerId);
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerRaisedEvent event) {
        UUID playerId = event.getPlayerId();
        int raiseToAmount = event.getRaiseToAmount();

        originatingBettorPlayerId = playerId;

        int raiseAboveCall = raiseToAmount
                - (chipsInFrontMap.get(playerId).intValue() + callAmountsMap
                        .get(playerId).intValue());
        int increaseOfChipsInFront = raiseToAmount
                - chipsInFrontMap.get(playerId).intValue();

        playersStillInHand.stream().filter(x -> !x.equals(playerId)).forEach(x -> {
            adjustPlayersFieldsAfterRaise(raiseToAmount, raiseAboveCall, x);
        });

        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap.put(playerId, Integer.valueOf(0));
        raiseToAmountsMap.put(playerId, Integer.valueOf(bigBlind));

        chipsInFrontMap.put(playerId, Integer.valueOf(raiseToAmount));

        int newChipsInBack = chipsInBackMap.get(playerId).intValue()
                - increaseOfChipsInFront;
        chipsInBackMap.put(playerId, Integer.valueOf(newChipsInBack));
    }

    void applyEvent(ActionOnChangedEvent event) {
        actionOnPosition = seatMap.entrySet().stream()
                .filter(x -> x.getValue().equals(event.getPlayerId())).findAny().get()
                .getKey().intValue();
    }

    void applyEvent(LastToActChangedEvent event) {
        lastToActPlayerId = event.getPlayerId();
    }

    void applyEvent(@SuppressWarnings("unused") FlopCardsDealtEvent event) {
        flopDealt = true;
    }

    void applyEvent(@SuppressWarnings("unused") TurnCardDealtEvent event) {
        turnDealt = true;
    }

    void applyEvent(@SuppressWarnings("unused") RiverCardDealtEvent event) {
        riverDealt = true;
    }

    private UUID findActionOnPlayerIdForNewRound() {
        int buttonIndex = buttonOnPosition;

        for (int i = buttonIndex + 1; i < seatMap.size(); i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable).intValue() != 0) {
                return playerAtTable;
            }
        }

        for (int i = 0; i < buttonIndex; i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable).intValue() != 0) {
                return playerAtTable;
            }
        }

        throw new FlexPokerException("couldn't determine new action on after round");
    }

    private void resetChipsInFront() {
        Set<UUID> playersInMap = chipsInFrontMap.keySet();
        playersInMap.forEach(x -> chipsInFrontMap.put(x, Integer.valueOf(0)));
    }

    private void resetCallAndRaiseAmountsAfterRound() {
        playersStillInHand
                .forEach(playerInHand -> {
                    callAmountsMap.put(playerInHand, Integer.valueOf(0));
                    if (bigBlind > chipsInBackMap.get(playerInHand)
                            .intValue()) {
                        raiseToAmountsMap.put(playerInHand,
                                chipsInBackMap.get(playerInHand));
                    } else {
                        raiseToAmountsMap.put(playerInHand,
                                Integer.valueOf(bigBlind));
                    }
                });
    }

    private void resetPossibleSeatActionsAfterRound() {
        playersStillInHand.forEach(playerInHand -> {
            possibleSeatActionsMap.get(playerInHand).clear();
            possibleSeatActionsMap.get(playerInHand).add(PlayerAction.CHECK);
            possibleSeatActionsMap.get(playerInHand).add(PlayerAction.RAISE);
        });
    }

    private UUID findNextToAct() {
        for (int i = actionOnPosition + 1; i < seatMap.size(); i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable;
            }
        }

        for (int i = 0; i < actionOnPosition; i++) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable;
            }
        }

        throw new IllegalStateException("unable to find next to act");
    }

    Optional<WinnersDeterminedEvent> determineWinnersIfAppropriate(int aggregateVersion) {
        if (handDealerState == HandDealerState.COMPLETE) {
            Set<UUID> playersRequiredToShowCards = potHandler.fetchPlayersRequriedToShowCards(playersStillInHand);
            Map<UUID, Integer> playersToChipsWonMap = potHandler.fetchChipsWon(playersStillInHand);
            return Optional.of(new WinnersDeterminedEvent(tableId, aggregateVersion,
                    gameId, entityId, playersRequiredToShowCards, playersToChipsWonMap));
        }

        return Optional.empty();
    }

    private void addToChipsInBack(UUID playerId, int chipsToAdd) {
        int currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(currentAmount + chipsToAdd));
    }

    private void subtractFromChipsInBack(UUID playerId, int chipsToSubtract) {
        int currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap.put(playerId, Integer.valueOf(currentAmount - chipsToSubtract));
    }

    private void subtractFromChipsInFront(UUID playerId, int chipsToSubtract) {
        int currentAmount = chipsInFrontMap.get(playerId).intValue();
        chipsInFrontMap.put(playerId, Integer.valueOf(currentAmount - chipsToSubtract));
    }

    private int determineLastToAct() {
        int seatIndex;

        if (originatingBettorPlayerId == null) {
            seatIndex = buttonOnPosition;
        } else {
            seatIndex = seatMap.entrySet().stream()
                    .filter(x -> x.getValue().equals(originatingBettorPlayerId))
                    .findAny().get().getKey().intValue();

            if (seatIndex == 0) {
                seatIndex = seatMap.size() - 1;
            } else {
                seatIndex--;
            }
        }

        for (int i = seatIndex; i >= 0; i--) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        for (int i = seatMap.size() - 1; i > seatIndex; i--) {
            UUID playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        throw new IllegalStateException("unable to determine last to act");
    }

    boolean idMatches(UUID handId) {
        return entityId.equals(handId);
    }

    private void adjustPlayersFieldsAfterRaise(int raiseToAmount, int raiseAboveCall,
            UUID playerId) {
        possibleSeatActionsMap.get(playerId).clear();
        possibleSeatActionsMap.get(playerId).add(PlayerAction.CALL);
        possibleSeatActionsMap.get(playerId).add(PlayerAction.FOLD);

        int totalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();

        if (totalChips <= raiseToAmount) {
            callAmountsMap.put(playerId, Integer.valueOf(totalChips));
            raiseToAmountsMap.put(playerId, Integer.valueOf(0));
        } else {
            callAmountsMap.put(
                    playerId,
                    Integer.valueOf(raiseToAmount
                            - chipsInFrontMap.get(playerId).intValue()));
            possibleSeatActionsMap.get(playerId).add(PlayerAction.RAISE);
            if (totalChips < raiseToAmount + raiseAboveCall) {
                raiseToAmountsMap.put(playerId, Integer.valueOf(totalChips));
            } else {
                raiseToAmountsMap.put(playerId,
                        Integer.valueOf(raiseToAmount + raiseAboveCall));
            }
        }
    }

    void applyEvent(PotAmountIncreasedEvent event) {
        potHandler.addToPot(event.getPotId(), event.getAmountIncreased());
        playersStillInHand.forEach(x -> subtractFromChipsInFront(x,
                event.getAmountIncreased()));
    }

    void applyEvent(PotClosedEvent event) {
        potHandler.closePot(event.getPotId());
    }

    void applyEvent(PotCreatedEvent event) {
        potHandler.addNewPot(event.getPotId(), event.getPlayersInvolved());
    }

    void applyEvent(RoundCompletedEvent event) {
        handDealerState = event.getNextHandDealerState();
        originatingBettorPlayerId = null;
        resetChipsInFront();
        resetCallAndRaiseAmountsAfterRound();
        resetPossibleSeatActionsAfterRound();
    }

    void applyEvent(WinnersDeterminedEvent event) {
        playersToShowCards.addAll(event.getPlayersToShowCards());
        event.getPlayersToChipsWonMap().forEach((playerId, chips) -> {
            addToChipsInBack(playerId, chips.intValue());
        });
    }

}
