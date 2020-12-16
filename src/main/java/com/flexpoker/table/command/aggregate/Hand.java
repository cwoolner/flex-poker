package com.flexpoker.table.command.aggregate;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.PlayerAction;
import com.flexpoker.table.command.FlopCards;
import com.flexpoker.table.command.PocketCards;
import com.flexpoker.table.command.RiverCard;
import com.flexpoker.table.command.TurnCard;
import com.flexpoker.table.command.aggregate.pot.PotHandler;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent;
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
import com.flexpoker.table.command.events.TableEvent;
import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    private PMap<UUID, Set<PlayerAction>> possibleSeatActionsMap;

    private UUID originatingBettorPlayerId;

    private UUID lastToActPlayerId;

    private HandDealerState handDealerState;

    private final List<HandEvaluation> handEvaluationList;

    private PSet<UUID> playersStillInHand;

    private PMap<UUID, Integer> chipsInBackMap;

    private PMap<UUID, Integer> chipsInFrontMap;

    private PMap<UUID, Integer> callAmountsMap;

    private PMap<UUID, Integer> raiseToAmountsMap;

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
        this.possibleSeatActionsMap = HashTreePMap.from(possibleSeatActionsMap);
        this.playersStillInHand = HashTreePSet.from(playersStillInHand);
        this.handDealerState = handDealerState;
        this.chipsInBackMap = HashTreePMap.from(chipsInBack);
        this.chipsInFrontMap = HashTreePMap.from(chipsInFrontMap);
        this.callAmountsMap = HashTreePMap.from(callAmountsMap);
        this.raiseToAmountsMap = HashTreePMap.from(raiseToAmountsMap);
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.playersToShowCards = new HashSet<>();
        this.potHandler = new PotHandler(gameId, tableId, entityId, handEvaluationList);
    }

    public List<TableEvent> dealHand(int actionOnPosition) {
        var eventsCreated = new ArrayList<TableEvent>();

        seatMap.keySet().stream().filter(x -> seatMap.get(x) != null)
                .forEach(seatPosition -> {
                    handleStartOfHandPlayerValues(seatPosition.intValue());
                });

        lastToActPlayerId = seatMap.get(Integer.valueOf(bigBlindPosition));
        handDealerState = HandDealerState.POCKET_CARDS_DEALT;

        var handDealtEvent = new HandDealtEvent(tableId, gameId, entityId, flopCards, turnCard, riverCard,
                buttonOnPosition, smallBlindPosition, bigBlindPosition, lastToActPlayerId, HashTreePMap.from(seatMap),
                HashTreePMap.from(playerToPocketCardsMap), HashTreePMap.from(possibleSeatActionsMap),
                HashTreePSet.from(playersStillInHand), handEvaluationList, handDealerState, HashTreePMap.from(chipsInBackMap),
                HashTreePMap.from(chipsInFrontMap), HashTreePMap.from(callAmountsMap), HashTreePMap.from(raiseToAmountsMap),
                smallBlind, bigBlind);
        eventsCreated.add(handDealtEvent);

        // creat an initial empty pot for the table
        eventsCreated.add(new PotCreatedEvent(tableId, gameId, entityId, UUID.randomUUID(), playersStillInHand));

        var actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        var actionOnChangedEvent = new ActionOnChangedEvent(tableId, gameId, entityId, actionOnPlayerId);
        eventsCreated.add(actionOnChangedEvent);

        return eventsCreated;
    }

    private void handleStartOfHandPlayerValues(int seatPosition) {
        var playerId = seatMap.get(Integer.valueOf(seatPosition));

        var chipsInFront = 0;
        var callAmount = bigBlind;
        var raiseToAmount = bigBlind * 2;

        if (seatPosition == bigBlindPosition) {
            chipsInFront = bigBlind;
            callAmount = 0;
        } else if (seatPosition == smallBlindPosition) {
            chipsInFront = smallBlind;
            callAmount = smallBlind;
        }

        if (chipsInFront > chipsInBackMap.get(playerId).intValue()) {
            chipsInFrontMap = chipsInFrontMap.plus(playerId, chipsInBackMap.get(playerId));
        } else {
            chipsInFrontMap = chipsInFrontMap.plus(playerId, Integer.valueOf(chipsInFront));
        }

        subtractFromChipsInBack(playerId, chipsInFrontMap.get(playerId).intValue());

        if (callAmount > chipsInBackMap.get(playerId).intValue()) {
            callAmountsMap = callAmountsMap.plus(playerId, chipsInBackMap.get(playerId));
        } else {
            callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(callAmount));
        }

        var totalChips = chipsInBackMap.get(playerId).intValue() + chipsInFrontMap.get(playerId).intValue();

        if (raiseToAmount > totalChips) {
            raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(totalChips));
        } else {
            raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(raiseToAmount));
        }

        if (raiseToAmountsMap.get(playerId).intValue() > 0) {
            var playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.RAISE);
        }

        if (callAmountsMap.get(playerId).intValue() > 0) {
            var playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.CALL);
            playerActions.add(PlayerAction.FOLD);
        } else {
            var playerActions = possibleSeatActionsMap.get(playerId);
            playerActions.add(PlayerAction.CHECK);
        }
    }

    public TableEvent check(UUID playerId, boolean forced) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CHECK);

        return forced
                ? new PlayerForceCheckedEvent(tableId, gameId, entityId, playerId)
                : new PlayerCheckedEvent(tableId, gameId, entityId, playerId);
    }

    public PlayerCalledEvent call(UUID playerId) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.CALL);

        return new PlayerCalledEvent(tableId, gameId, entityId, playerId);
    }

    public TableEvent fold(UUID playerId, boolean forced) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.FOLD);

        return forced
                ? new PlayerForceFoldedEvent(tableId, gameId, entityId, playerId)
                : new PlayerFoldedEvent(tableId, gameId, entityId, playerId);
    }

    public PlayerRaisedEvent raise(UUID playerId, int raiseToAmount) {
        checkActionOnPlayer(playerId);
        checkPerformAction(playerId, PlayerAction.RAISE);
        checkRaiseAmountValue(playerId, raiseToAmount);

        return new PlayerRaisedEvent(tableId, gameId, entityId, playerId, raiseToAmount);
    }

    TableEvent expireActionOn(UUID playerId) {
        if (callAmountsMap.get(playerId).intValue() == 0) {
            return check(playerId, true);
        }

        return fold(playerId, true);
    }

    public List<TableEvent> changeActionOn() {
        if (chipsInBackMap.values().stream().allMatch(x -> x == 0)) {
            return Collections.emptyList();
        }

        var eventsCreated = new ArrayList<TableEvent>();

        // do not change action on if the hand is over. starting a new hand
        // should adjust that
        if (handDealerState == HandDealerState.COMPLETE) {
            return eventsCreated;
        }

        var actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));

        // the player just bet, so a new last to act needs to be determined
        if (actionOnPlayerId.equals(originatingBettorPlayerId)) {
            var nextPlayerToAct = findNextToAct();
            var lastPlayerToAct = seatMap.get(Integer.valueOf(determineLastToAct()));

            var actionOnChangedEvent = new ActionOnChangedEvent(tableId, gameId, entityId, nextPlayerToAct);
            var lastToActChangedEvent = new LastToActChangedEvent(tableId, gameId, entityId, lastPlayerToAct);

            eventsCreated.add(actionOnChangedEvent);
            eventsCreated.add(lastToActChangedEvent);

        }
        // if it's the last player to act, recalculate for a new round
        else if (actionOnPlayerId.equals(lastToActPlayerId)) {
            var nextPlayerToAct = findActionOnPlayerIdForNewRound();
            var newRoundLastPlayerToAct = seatMap.get(Integer.valueOf(determineLastToAct()));

            var actionOnChangedEvent = new ActionOnChangedEvent(tableId, gameId, entityId, nextPlayerToAct);
            var lastToActChangedEvent = new LastToActChangedEvent(tableId, gameId, entityId, newRoundLastPlayerToAct);
            eventsCreated.add(actionOnChangedEvent);
            eventsCreated.add(lastToActChangedEvent);
        }
        // just a normal transition mid-round
        else {
            var nextPlayerToAct = findNextToAct();
            var actionOnChangedEvent = new ActionOnChangedEvent(tableId, gameId, entityId, nextPlayerToAct);
            eventsCreated.add(actionOnChangedEvent);
        }

        return eventsCreated;
    }

    Optional<TableEvent> dealCommonCardsIfAppropriate() {
        if (handDealerState == HandDealerState.FLOP_DEALT && !flopDealt) {
            return Optional.of(new FlopCardsDealtEvent(tableId, gameId, entityId));
        }

        if (handDealerState == HandDealerState.TURN_DEALT && !turnDealt) {
            return Optional.of(new TurnCardDealtEvent(tableId, gameId, entityId));
        }

        if (handDealerState == HandDealerState.RIVER_DEALT && !riverDealt) {
            return Optional.of(new RiverCardDealtEvent(tableId, gameId, entityId));
        }

        return Optional.empty();
    }

    List<TableEvent> handlePotAndRoundCompleted() {
        if (!seatMap.get(Integer.valueOf(actionOnPosition)).equals(lastToActPlayerId)
                && playersStillInHand.size() > 1) {
            return Collections.emptyList();
        }

        var tableEvents = new ArrayList<TableEvent>();
        tableEvents.addAll(potHandler.calculatePots(chipsInFrontMap, chipsInBackMap));

        var nextHandDealerState = playersStillInHand.size() == 1 ? HandDealerState.COMPLETE
                : HandDealerState.values()[handDealerState.ordinal() + 1];

        var roundCompletedEvent = new RoundCompletedEvent(tableId, gameId, entityId, nextHandDealerState);
        tableEvents.add(roundCompletedEvent);
        applyEvent(roundCompletedEvent);

        return tableEvents;
    }

    Optional<TableEvent> finishHandIfAppropriate() {
        if (handDealerState == HandDealerState.COMPLETE) {
            return Optional.of(new HandCompletedEvent(tableId, gameId, entityId, chipsInBackMap));
        }

        return Optional.empty();
    }

    private void checkRaiseAmountValue(UUID playerId, int raiseToAmount) {
        var playersTotalChips = chipsInBackMap.get(playerId).intValue() + chipsInFrontMap.get(playerId).intValue();
        if (raiseToAmount < raiseToAmountsMap.get(playerId).intValue() || raiseToAmount > playersTotalChips) {
            throw new IllegalArgumentException("Raise amount must be between the minimum and maximum values.");
        }
    }

    private void checkActionOnPlayer(UUID playerId) {
        var actionOnPlayerId = seatMap.get(Integer.valueOf(actionOnPosition));
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
        var playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(0));
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerForceCheckedEvent event) {
        var playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(0));
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerCalledEvent event) {
        var playerId = event.getPlayerId();
        possibleSeatActionsMap.get(playerId).clear();

        var newChipsInFront = chipsInFrontMap.get(playerId).intValue() + callAmountsMap.get(playerId).intValue();
        chipsInFrontMap = chipsInFrontMap.plus(playerId, Integer.valueOf(newChipsInFront));

        var newChipsInBack = chipsInBackMap.get(playerId).intValue() - callAmountsMap.get(playerId).intValue();
        chipsInBackMap = chipsInBackMap.plus(playerId, Integer.valueOf(newChipsInBack));

        callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(0));
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerFoldedEvent event) {
        var playerId = event.getPlayerId();
        playersStillInHand = playersStillInHand.minus(playerId);
        potHandler.removePlayerFromAllPots(playerId);
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(0));
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerForceFoldedEvent event) {
        var playerId = event.getPlayerId();
        playersStillInHand = playersStillInHand.minus(playerId);
        potHandler.removePlayerFromAllPots(playerId);
        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(0));
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(0));
    }

    void applyEvent(PlayerRaisedEvent event) {
        var playerId = event.getPlayerId();
        var raiseToAmount = event.getRaiseToAmount();

        originatingBettorPlayerId = playerId;

        var raiseAboveCall = raiseToAmount
                - (chipsInFrontMap.get(playerId).intValue() + callAmountsMap.get(playerId).intValue());
        var increaseOfChipsInFront = raiseToAmount - chipsInFrontMap.get(playerId).intValue();

        playersStillInHand.stream().filter(x -> !x.equals(playerId)).forEach(x -> {
            adjustPlayersFieldsAfterRaise(raiseToAmount, raiseAboveCall, x);
        });

        possibleSeatActionsMap.get(playerId).clear();
        callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(0));
        raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(bigBlind));

        chipsInFrontMap = chipsInFrontMap.plus(playerId, Integer.valueOf(raiseToAmount));

        var newChipsInBack = chipsInBackMap.get(playerId).intValue()
                - increaseOfChipsInFront;
        chipsInBackMap = chipsInBackMap.plus(playerId, Integer.valueOf(newChipsInBack));
    }

    void applyEvent(ActionOnChangedEvent event) {
        actionOnPosition = seatMap.entrySet().stream()
                .filter(x -> x.getValue().equals(event.getPlayerId()))
                .findAny()
                .get()
                .getKey()
                .intValue();
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
        var buttonIndex = buttonOnPosition;

        for (var i = buttonIndex + 1; i < seatMap.size(); i++) {
            var playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable).intValue() != 0) {
                return playerAtTable;
            }
        }

        for (var i = 0; i < buttonIndex; i++) {
            var playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)
                    && chipsInBackMap.get(playerAtTable).intValue() != 0) {
                return playerAtTable;
            }
        }

        throw new FlexPokerException("couldn't determine new action on after round");
    }

    private void resetChipsInFront() {
        var playersInMap = chipsInFrontMap.keySet();
        playersInMap.forEach(x -> {
            chipsInFrontMap = chipsInFrontMap.plus(x, Integer.valueOf(0));
        });
    }

    private void resetCallAndRaiseAmountsAfterRound() {
        playersStillInHand
                .forEach(playerInHand -> {
                    callAmountsMap = callAmountsMap.plus(playerInHand, Integer.valueOf(0));
                    if (bigBlind > chipsInBackMap.get(playerInHand).intValue()) {
                        raiseToAmountsMap = raiseToAmountsMap.plus(playerInHand, chipsInBackMap.get(playerInHand));
                    } else {
                        raiseToAmountsMap = raiseToAmountsMap.plus(playerInHand, Integer.valueOf(bigBlind));
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
        for (var i = actionOnPosition + 1; i < seatMap.size(); i++) {
            var playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable;
            }
        }

        for (var i = 0; i < actionOnPosition; i++) {
            var playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return playerAtTable;
            }
        }

        throw new IllegalStateException("unable to find next to act");
    }

    Optional<WinnersDeterminedEvent> determineWinnersIfAppropriate() {
        if (handDealerState == HandDealerState.COMPLETE) {
            var playersRequiredToShowCards = potHandler.fetchPlayersRequiredToShowCards(playersStillInHand);
            var playersToChipsWonMap = potHandler.fetchChipsWon(playersStillInHand);
            return Optional.of(new WinnersDeterminedEvent(tableId, gameId, entityId, playersRequiredToShowCards,
                    playersToChipsWonMap));
        }

        return Optional.empty();
    }

    private void addToChipsInBack(UUID playerId, int chipsToAdd) {
        var currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap = chipsInBackMap.plus(playerId, Integer.valueOf(currentAmount + chipsToAdd));
    }

    private void subtractFromChipsInBack(UUID playerId, int chipsToSubtract) {
        var currentAmount = chipsInBackMap.get(playerId).intValue();
        chipsInBackMap = chipsInBackMap.plus(playerId, Integer.valueOf(currentAmount - chipsToSubtract));
    }

    private void subtractFromChipsInFront(UUID playerId, int chipsToSubtract) {
        var currentAmount = chipsInFrontMap.get(playerId).intValue();
        chipsInFrontMap = chipsInFrontMap.plus(playerId, Integer.valueOf(currentAmount - chipsToSubtract));
    }

    private int determineLastToAct() {
        int seatIndex;

        if (originatingBettorPlayerId == null) {
            seatIndex = buttonOnPosition;
        } else {
            seatIndex = seatMap.entrySet().stream()
                    .filter(x -> x.getValue().equals(originatingBettorPlayerId))
                    .findAny()
                    .get()
                    .getKey()
                    .intValue();

            if (seatIndex == 0) {
                seatIndex = seatMap.size() - 1;
            } else {
                seatIndex--;
            }
        }

        for (var i = seatIndex; i >= 0; i--) {
            var playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        for (var i = seatMap.size() - 1; i > seatIndex; i--) {
            var playerAtTable = seatMap.get(Integer.valueOf(i));
            if (playerAtTable != null && playersStillInHand.contains(playerAtTable)) {
                return i;
            }
        }

        throw new IllegalStateException("unable to determine last to act");
    }

    boolean idMatches(UUID handId) {
        return entityId.equals(handId);
    }

    private void adjustPlayersFieldsAfterRaise(int raiseToAmount, int raiseAboveCall, UUID playerId) {
        possibleSeatActionsMap.get(playerId).clear();
        possibleSeatActionsMap.get(playerId).add(PlayerAction.CALL);
        possibleSeatActionsMap.get(playerId).add(PlayerAction.FOLD);

        var totalChips = chipsInBackMap.get(playerId).intValue()
                + chipsInFrontMap.get(playerId).intValue();

        if (totalChips <= raiseToAmount) {
            callAmountsMap = callAmountsMap.plus(playerId, totalChips - chipsInFrontMap.get(playerId));
            raiseToAmountsMap = raiseToAmountsMap.plus(playerId, 0);
        } else {
            callAmountsMap = callAmountsMap.plus(playerId, Integer.valueOf(raiseToAmount - chipsInFrontMap.get(playerId).intValue()));
            possibleSeatActionsMap.get(playerId).add(PlayerAction.RAISE);
            if (totalChips < raiseToAmount + raiseAboveCall) {
                raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(totalChips));
            } else {
                raiseToAmountsMap = raiseToAmountsMap.plus(playerId, Integer.valueOf(raiseToAmount + raiseAboveCall));
            }
        }
    }

    void applyEvent(PotAmountIncreasedEvent event) {
        potHandler.addToPot(event.getPotId(), event.getAmountIncreased());
        playersStillInHand.forEach(x -> subtractFromChipsInFront(x, event.getAmountIncreased()));
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

    Optional<TableEvent> autoMoveHandForward() {
        return chipsInBackMap.values().stream().allMatch(x -> x == 0)
                ? Optional.of(new AutoMoveHandForwardEvent(tableId, gameId, entityId))
                : Optional.empty();
    }

}
