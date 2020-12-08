package com.flexpoker.table.command.aggregate;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.command.EventApplier;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerAddedEvent;
import com.flexpoker.table.command.events.PlayerBustedTableEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerForceCheckedEvent;
import com.flexpoker.table.command.events.PlayerForceFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PlayerRemovedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TablePausedEvent;
import com.flexpoker.table.command.events.TableResumedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.command.framework.TableEvent;
import org.pcollections.HashTreePMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Table {

    private final List<TableEvent> newEvents;

    private final List<TableEvent> appliedEvents;

    private final UUID aggregateId;

    private final Map<Class<? extends TableEvent>, EventApplier<? super TableEvent>> methodTable;

    private final UUID gameId;

    private final Map<Integer, UUID> seatMap;

    private final Map<UUID, Integer> chipsInBack;

    private int buttonOnPosition;

    private int smallBlindPosition;

    private int bigBlindPosition;

    private Hand currentHand;

    private boolean paused;

    protected Table(boolean creatingFromEvents, UUID aggregateId, UUID gameId,
            Map<Integer, UUID> seatMap, int startingNumberOfChips) {
        this.aggregateId = aggregateId;
        this.gameId = gameId;
        this.seatMap = seatMap;
        this.chipsInBack = new HashMap<>();

        seatMap.values().stream().filter(x -> x != null)
                .forEach(x -> chipsInBack.put(x, startingNumberOfChips));

        newEvents = new ArrayList<>();
        appliedEvents = new ArrayList<>();

        methodTable = new HashMap<>();
        populateMethodTable();

        if (!creatingFromEvents) {
            var tableCreatedEvent = new TableCreatedEvent(aggregateId, gameId, seatMap.size(),
                    HashTreePMap.from(seatMap), startingNumberOfChips);
            newEvents.add(tableCreatedEvent);
            applyCommonEvent(tableCreatedEvent);
        }
    }

    public List<TableEvent> fetchNewEvents() {
        return new ArrayList<>(newEvents);
    }

    public List<TableEvent> fetchAppliedEvents() {
        return new ArrayList<>(appliedEvents);
    }

    public void applyAllHistoricalEvents(List<TableEvent> events) {
        events.forEach(x -> {
            applyCommonEvent(x);
        });
    }

    private void populateMethodTable() {
        methodTable.put(TableCreatedEvent.class, x -> {});
        methodTable.put(CardsShuffledEvent.class, x -> {});
        methodTable.put(HandDealtEvent.class, x -> applyHandDealtEvent((HandDealtEvent) x));
        methodTable.put(AutoMoveHandForwardEvent.class, x -> {});
        methodTable.put(PlayerCalledEvent.class, x -> currentHand.applyEvent((PlayerCalledEvent) x));
        methodTable.put(PlayerCheckedEvent.class, x -> currentHand.applyEvent((PlayerCheckedEvent) x));
        methodTable.put(PlayerForceCheckedEvent.class, x -> currentHand.applyEvent((PlayerForceCheckedEvent) x));
        methodTable.put(PlayerFoldedEvent.class, x -> currentHand.applyEvent((PlayerFoldedEvent) x));
        methodTable.put(PlayerForceFoldedEvent.class, x -> currentHand.applyEvent((PlayerForceFoldedEvent) x));
        methodTable.put(PlayerRaisedEvent.class, x -> currentHand.applyEvent((PlayerRaisedEvent) x));
        methodTable.put(FlopCardsDealtEvent.class, x -> currentHand.applyEvent((FlopCardsDealtEvent) x));
        methodTable.put(TurnCardDealtEvent.class, x -> currentHand.applyEvent((TurnCardDealtEvent) x));
        methodTable.put(RiverCardDealtEvent.class, x -> currentHand.applyEvent((RiverCardDealtEvent) x));
        methodTable.put(PotAmountIncreasedEvent.class, x -> currentHand.applyEvent((PotAmountIncreasedEvent) x));
        methodTable.put(PotClosedEvent.class, x -> currentHand.applyEvent((PotClosedEvent) x));
        methodTable.put(PotCreatedEvent.class, x -> currentHand.applyEvent((PotCreatedEvent) x));
        methodTable.put(RoundCompletedEvent.class, x -> currentHand.applyEvent((RoundCompletedEvent) x));
        methodTable.put(ActionOnChangedEvent.class, x -> currentHand.applyEvent((ActionOnChangedEvent) x));
        methodTable.put(LastToActChangedEvent.class, x -> currentHand.applyEvent((LastToActChangedEvent) x));
        methodTable.put(WinnersDeterminedEvent.class, x -> currentHand.applyEvent((WinnersDeterminedEvent) x));
        methodTable.put(HandCompletedEvent.class, x -> {
            var event = (HandCompletedEvent) x;
            currentHand = null;
            chipsInBack.clear();
            chipsInBack.putAll(new HashMap<>(event.getPlayerToChipsAtTableMap()));
        });
        methodTable.put(TablePausedEvent.class, x -> paused = true);
        methodTable.put(TableResumedEvent.class, x -> paused = false);
        methodTable.put(PlayerAddedEvent.class, x -> {
            var event = (PlayerAddedEvent) x;
            seatMap.put(event.getPosition(), event.getPlayerId());
            chipsInBack.put(event.getPlayerId(), event.getChipsInBack());
        });
        methodTable.put(PlayerRemovedEvent.class, x -> {
            var event = (PlayerRemovedEvent) x;
            var position = seatMap.entrySet().stream()
                    .filter(y -> y.getValue().equals(event.getPlayerId()))
                    .findFirst().get().getKey();
            seatMap.remove(position);
            chipsInBack.remove(event.getPlayerId());
        });
        methodTable.put(PlayerBustedTableEvent.class, x -> {
            var event = (PlayerBustedTableEvent) x;
            var position = seatMap.entrySet().stream()
                    .filter(y -> y.getValue().equals(event.getPlayerId()))
                    .findFirst().get().getKey();
            seatMap.remove(position);
            chipsInBack.remove(event.getPlayerId());
        });
    }

    private void applyCommonEvent(TableEvent event) {
        methodTable.get(event.getClass()).applyEvent(event);
        appliedEvents.add(event);
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    private void applyHandDealtEvent(HandDealtEvent event) {
        buttonOnPosition = event.getButtonOnPosition();
        smallBlindPosition = event.getSmallBlindPosition();
        bigBlindPosition = event.getBigBlindPosition();
        currentHand = new Hand(event.getGameId(), event.getAggregateId(),
                event.getHandId(), seatMap, event.getFlopCards(), event.getTurnCard(),
                event.getRiverCard(), event.getButtonOnPosition(),
                event.getSmallBlindPosition(), event.getBigBlindPosition(),
                event.getLastToActPlayerId(), event.getPlayerToPocketCardsMap(),
                event.getPossibleSeatActionsMap(), event.getPlayersStillInHand(),
                event.getHandEvaluations(), event.getHandDealerState(),
                event.getChipsInBack(), event.getChipsInFrontMap(),
                event.getCallAmountsMap(), event.getRaiseToAmountsMap(),
                event.getSmallBlind(), event.getBigBlind());
    }

    public void startNewHandForNewGame(int smallBlind, int bigBlind,
            List<Card> shuffledDeckOfCards, CardsUsedInHand cardsUsedInHand,
            Map<PocketCards, HandEvaluation> handEvaluations) {
        buttonOnPosition = assignButtonOnForNewGame();
        smallBlindPosition = assignSmallBlindForNewGame();
        bigBlindPosition = assignBigBlindForNewGame();
        int actionOnPosition = assignActionOnForNewHand();

        performNewHandCommonLogic(smallBlind, bigBlind, shuffledDeckOfCards,
                cardsUsedInHand, handEvaluations, actionOnPosition);
    }

    public void startNewHandForExistingTable(int smallBlind, int bigBlind,
            List<Card> shuffledDeckOfCards, CardsUsedInHand cardsUsedInHand,
            Map<PocketCards, HandEvaluation> handEvaluations) {
        buttonOnPosition = assignButtonOnForNewHand();
        smallBlindPosition = assignSmallBlindForNewHand();
        bigBlindPosition = assignBigBlindForNewHand();
        int actionOnPosition = assignActionOnForNewHand();

        performNewHandCommonLogic(smallBlind, bigBlind, shuffledDeckOfCards,
                cardsUsedInHand, handEvaluations, actionOnPosition);
    }

    private int assignButtonOnForNewGame() {
        while (true) {
            var potentialButtonOnPosition = new Random().nextInt(seatMap.size());
            if (seatMap.get(Integer.valueOf(potentialButtonOnPosition)) != null) {
                return potentialButtonOnPosition;
            }
        }
    }

    private int assignSmallBlindForNewGame() {
        return getNumberOfPlayersAtTable() == 2 ? buttonOnPosition
                : findNextFilledSeat(buttonOnPosition);
    }

    private int assignBigBlindForNewGame() {
        return getNumberOfPlayersAtTable() == 2 ? findNextFilledSeat(buttonOnPosition)
                : findNextFilledSeat(smallBlindPosition);
    }

    private int assignActionOnForNewHand() {
        return findNextFilledSeat(bigBlindPosition);
    }

    private int assignBigBlindForNewHand() {
        return findNextFilledSeat(bigBlindPosition);
    }

    private int assignSmallBlindForNewHand() {
        return findNextFilledSeat(smallBlindPosition);
    }

    private int assignButtonOnForNewHand() {
        return findNextFilledSeat(buttonOnPosition);
    }

    private int findNextFilledSeat(int startingPosition) {
        for (var i = startingPosition + 1; i < seatMap.size(); i++) {
            if (seatMap.get(Integer.valueOf(i)) != null) {
                return i;
            }
        }

        for (var i = 0; i < startingPosition; i++) {
            if (seatMap.get(Integer.valueOf(i)) != null) {
                return i;
            }
        }

        throw new IllegalStateException("unable to find next filled seat");
    }

    private void performNewHandCommonLogic(int smallBlind, int bigBlind,
            List<Card> shuffledDeckOfCards, CardsUsedInHand cardsUsedInHand,
            Map<PocketCards, HandEvaluation> handEvaluations,
            int actionOnPosition) {
        var cardsShuffledEvent = new CardsShuffledEvent(aggregateId, gameId, shuffledDeckOfCards);
        newEvents.add(cardsShuffledEvent);
        applyCommonEvent(cardsShuffledEvent);

        var nextToReceivePocketCards = findNextFilledSeat(buttonOnPosition);
        var playerToPocketCardsMap = new HashMap<UUID, PocketCards>();

        for (PocketCards pocketCards : cardsUsedInHand.getPocketCards()) {
            UUID playerIdAtPosition = seatMap.get(Integer
                    .valueOf(nextToReceivePocketCards));
            playerToPocketCardsMap.put(playerIdAtPosition, pocketCards);
            nextToReceivePocketCards = findNextFilledSeat(nextToReceivePocketCards);
            handEvaluations.get(pocketCards).setPlayerId(playerIdAtPosition);
        }

        var playersStillInHand = seatMap.values().stream().filter(x -> x != null)
                .collect(Collectors.toSet());
        var possibleSeatActionsMap = new HashMap<UUID, Set<PlayerAction>>();
        playersStillInHand.forEach(x -> possibleSeatActionsMap.put(x,
                new HashSet<PlayerAction>()));

        var hand = new Hand(gameId, aggregateId, UUID.randomUUID(), seatMap,
                cardsUsedInHand.getFlopCards(), cardsUsedInHand.getTurnCard(),
                cardsUsedInHand.getRiverCard(), buttonOnPosition, smallBlindPosition,
                bigBlindPosition, null, playerToPocketCardsMap, possibleSeatActionsMap,
                playersStillInHand, new ArrayList<>(handEvaluations.values()),
                HandDealerState.NONE, chipsInBack, new HashMap<>(), new HashMap<>(),
                new HashMap<>(), smallBlind, bigBlind);
        var eventsCreated = hand.dealHand(actionOnPosition);
        eventsCreated.forEach(x -> {
            newEvents.add(x);
            applyCommonEvent(x);
        });
    }

    public int getNumberOfPlayersAtTable() {
        return (int) seatMap.values().stream().filter(x -> x != null).count();
    }

    public void check(UUID playerId) {
        checkHandIsBeingPlayed();

        var playerCheckedEvent = currentHand.check(playerId, false);
        newEvents.add(playerCheckedEvent);
        applyCommonEvent(playerCheckedEvent);

        handleEndOfRound();
    }

    public void call(UUID playerId) {
        checkHandIsBeingPlayed();

        var playerCalledEvent = currentHand.call(playerId);
        newEvents.add(playerCalledEvent);
        applyCommonEvent(playerCalledEvent);

        handleEndOfRound();
    }

    public void fold(UUID playerId) {
        checkHandIsBeingPlayed();

        var playerFoldedEvent = currentHand.fold(playerId, false);
        newEvents.add(playerFoldedEvent);
        applyCommonEvent(playerFoldedEvent);

        handleEndOfRound();
    }

    public void raise(UUID playerId, int raiseToAmount) {
        checkHandIsBeingPlayed();

        var playerRaisedEvent = currentHand.raise(playerId, raiseToAmount);
        newEvents.add(playerRaisedEvent);
        applyCommonEvent(playerRaisedEvent);

        changeActionOnIfAppropriate();
    }

    public void expireActionOn(UUID handId, UUID playerId) {
        checkHandIsBeingPlayed();

        if (!currentHand.idMatches(handId)) {
            return;
        }

        var forcedActionOnExpiredEvent = currentHand.expireActionOn(playerId);
        newEvents.add(forcedActionOnExpiredEvent);
        applyCommonEvent(forcedActionOnExpiredEvent);

        handleEndOfRound();
    }

    public void autoMoveHandForward() {
        checkHandIsBeingPlayed();
        handleEndOfRound();
    }

    private void handleEndOfRound() {
        handlePotAndRoundCompleted();
        changeActionOnIfAppropriate();
        dealCommonCardsIfAppropriate();
        determineWinnersIfAppropriate();
        removeAnyBustedPlayers();
        finishHandIfAppropriate();
    }

    private void checkHandIsBeingPlayed() {
        if (currentHand == null) {
            throw new FlexPokerException("no hand in progress");
        }
    }

    private void handlePotAndRoundCompleted() {
        var endOfRoundEvents = currentHand.handlePotAndRoundCompleted();
        endOfRoundEvents.forEach(x -> {
            newEvents.add(x);
            // TODO: not using applyCommonEvent() here because PotHandler is too
            // stateful and the events get applied to the state down there. when
            // that's refactored, this should change
            appliedEvents.add(x);
        });
    }

    private void changeActionOnIfAppropriate() {
        var actionOnChangedEvents = currentHand.changeActionOn();
        actionOnChangedEvents.forEach(x -> {
            newEvents.add(x);
            applyCommonEvent(x);
        });
    }

    private void dealCommonCardsIfAppropriate() {
        currentHand.dealCommonCardsIfAppropriate().ifPresent(
                event -> {
                    newEvents.add(event);
                    applyCommonEvent(event);
                });
    }

    private void determineWinnersIfAppropriate() {
        currentHand.determineWinnersIfAppropriate().ifPresent(
                event -> {
                    newEvents.add(event);
                    applyCommonEvent(event);
                });
    }

    private void removeAnyBustedPlayers() {
        chipsInBack.entrySet().stream()
            .filter(x -> x.getValue() == 0)
            .forEach(x -> {
                var event = new PlayerBustedTableEvent(aggregateId, gameId, x.getKey());
                newEvents.add(event);
                applyCommonEvent(event);
            });
    }

    private void finishHandIfAppropriate() {
        var handCompleteEvent = currentHand.finishHandIfAppropriate();

        if (handCompleteEvent.isPresent()) {
            newEvents.add(handCompleteEvent.get());
            applyCommonEvent(handCompleteEvent.get());
        } else {
            currentHand.autoMoveHandForward().ifPresent(event -> {
                newEvents.add(event);
                applyCommonEvent(event);
            });
        }
    }

    public void addPlayer(UUID playerId, int chips) {
        if (seatMap.values().contains(playerId)) {
            throw new FlexPokerException("player already at this table");
        }

        var newPlayerPosition = findRandomOpenSeat();

        var playerAddedEvent = new PlayerAddedEvent(aggregateId, gameId, playerId, chips, newPlayerPosition);
        newEvents.add(playerAddedEvent);
        applyCommonEvent(playerAddedEvent);
    }

    public void removePlayer(UUID playerId) {
        if (!seatMap.values().contains(playerId)) {
            throw new FlexPokerException("player not at this table");
        }

        if (currentHand != null) {
            throw new FlexPokerException("can't remove a player while in a hand");
        }

        var playerRemovedEvent = new PlayerRemovedEvent(aggregateId, gameId, playerId);
        newEvents.add(playerRemovedEvent);
        applyCommonEvent(playerRemovedEvent);
    }

    public void pause() {
        if (paused) {
            throw new FlexPokerException("table is already paused.  can't pause again.");
        }

        var tablePausedEvent = new TablePausedEvent(aggregateId, gameId);
        newEvents.add(tablePausedEvent);
        applyCommonEvent(tablePausedEvent);
    }

    public void resume() {
        if (!paused) {
            throw new FlexPokerException("table is not paused.  can't resume.");
        }

        var tableResumedEvent = new TableResumedEvent(aggregateId, gameId);
        newEvents.add(tableResumedEvent);
        applyCommonEvent(tableResumedEvent);
    }

    private int findRandomOpenSeat() {
        while (true) {
            var potentialNewPlayerPosition = new Random().nextInt(seatMap.size());
            if (seatMap.get(potentialNewPlayerPosition) == null) {
                return potentialNewPlayerPosition;
            }
        }
    }

}
