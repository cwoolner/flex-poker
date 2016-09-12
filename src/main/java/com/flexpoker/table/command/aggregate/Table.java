package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.command.EventApplier;
import com.flexpoker.framework.domain.AggregateRoot;
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

public class Table extends AggregateRoot<TableEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

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

        methodTable = new HashMap<>();
        populateMethodTable();

        if (!creatingFromEvents) {
            TableCreatedEvent tableCreatedEvent = new TableCreatedEvent(
                    aggregateId, ++aggregateVersion, gameId, seatMap.size(),
                    seatMap, startingNumberOfChips);
            addNewEvent(tableCreatedEvent);
            applyCommonEvent(tableCreatedEvent);
        }
    }

    @Override
    public void applyAllHistoricalEvents(List<TableEvent> events) {
        events.forEach(x -> {
            aggregateVersion++;
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
        methodTable.put(HandCompletedEvent.class, x -> currentHand = null);
        methodTable.put(TablePausedEvent.class, x -> paused = true);
        methodTable.put(TableResumedEvent.class, x -> paused = false);
        methodTable.put(PlayerAddedEvent.class, x -> {
            PlayerAddedEvent event = (PlayerAddedEvent) x;
            seatMap.put(event.getPosition(), event.getPlayerId());
            chipsInBack.put(event.getPlayerId(), event.getChipsInBack());
        });
        methodTable.put(PlayerRemovedEvent.class, x -> {
            PlayerRemovedEvent event = (PlayerRemovedEvent) x;
            Integer position = seatMap.entrySet().stream()
                    .filter(y -> y.getValue().equals(event.getPlayerId()))
                    .findFirst().get().getKey();
            seatMap.remove(position);
            chipsInBack.remove(event.getPlayerId());
        });
        methodTable.put(PlayerBustedTableEvent.class, x -> {
            PlayerBustedTableEvent event = (PlayerBustedTableEvent) x;
            Integer position = seatMap.entrySet().stream()
                    .filter(y -> y.getValue().equals(event.getPlayerId()))
                    .findFirst().get().getKey();
            seatMap.remove(position);
            chipsInBack.remove(event.getPlayerId());
        });
    }

    private void applyCommonEvent(TableEvent event) {
        methodTable.get(event.getClass()).applyEvent(event);
        addAppliedEvent(event);
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
            int potentialButtonOnPosition = new Random().nextInt(seatMap.size());
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
        for (int i = startingPosition + 1; i < seatMap.size(); i++) {
            if (seatMap.get(Integer.valueOf(i)) != null) {
                return i;
            }
        }

        for (int i = 0; i < startingPosition; i++) {
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
        CardsShuffledEvent cardsShuffledEvent = new CardsShuffledEvent(aggregateId,
                ++aggregateVersion, gameId, shuffledDeckOfCards);
        addNewEvent(cardsShuffledEvent);
        applyCommonEvent(cardsShuffledEvent);

        int nextToReceivePocketCards = findNextFilledSeat(buttonOnPosition);
        Map<UUID, PocketCards> playerToPocketCardsMap = new HashMap<>();

        for (PocketCards pocketCards : cardsUsedInHand.getPocketCards()) {
            UUID playerIdAtPosition = seatMap.get(Integer
                    .valueOf(nextToReceivePocketCards));
            playerToPocketCardsMap.put(playerIdAtPosition, pocketCards);
            nextToReceivePocketCards = findNextFilledSeat(nextToReceivePocketCards);
            handEvaluations.get(pocketCards).setPlayerId(playerIdAtPosition);
        }

        Set<UUID> playersStillInHand = seatMap.values().stream().filter(x -> x != null)
                .collect(Collectors.toSet());
        Map<UUID, Set<PlayerAction>> possibleSeatActionsMap = new HashMap<>();
        playersStillInHand.forEach(x -> possibleSeatActionsMap.put(x,
                new HashSet<PlayerAction>()));

        Hand hand = new Hand(gameId, aggregateId, UUID.randomUUID(), seatMap,
                cardsUsedInHand.getFlopCards(), cardsUsedInHand.getTurnCard(),
                cardsUsedInHand.getRiverCard(), buttonOnPosition, smallBlindPosition,
                bigBlindPosition, null, playerToPocketCardsMap, possibleSeatActionsMap,
                playersStillInHand, new ArrayList<>(handEvaluations.values()),
                HandDealerState.NONE, chipsInBack, new HashMap<>(), new HashMap<>(),
                new HashMap<>(), smallBlind, bigBlind);
        List<TableEvent> eventsCreated = hand.dealHand(aggregateVersion + 1,
                actionOnPosition);
        eventsCreated.forEach(x -> {
            addNewEvent(x);
            applyCommonEvent(x);
            aggregateVersion++;
        });
    }

    public int getNumberOfPlayersAtTable() {
        return (int) seatMap.values().stream().filter(x -> x != null).count();
    }

    public void check(UUID playerId) {
        checkHandIsBeingPlayed();

        TableEvent playerCheckedEvent = currentHand.check(playerId, false,
                ++aggregateVersion);
        addNewEvent(playerCheckedEvent);
        applyCommonEvent(playerCheckedEvent);

        handleEndOfRound();
    }

    public void call(UUID playerId) {
        checkHandIsBeingPlayed();

        PlayerCalledEvent playerCalledEvent = currentHand.call(playerId,
                ++aggregateVersion);
        addNewEvent(playerCalledEvent);
        applyCommonEvent(playerCalledEvent);

        handleEndOfRound();
    }

    public void fold(UUID playerId) {
        checkHandIsBeingPlayed();

        TableEvent playerFoldedEvent = currentHand.fold(playerId, false,
                ++aggregateVersion);
        addNewEvent(playerFoldedEvent);
        applyCommonEvent(playerFoldedEvent);

        handleEndOfRound();
    }

    public void raise(UUID playerId, int raiseToAmount) {
        checkHandIsBeingPlayed();

        PlayerRaisedEvent playerRaisedEvent = currentHand.raise(playerId,
                ++aggregateVersion, raiseToAmount);
        addNewEvent(playerRaisedEvent);
        applyCommonEvent(playerRaisedEvent);

        changeActionOnIfAppropriate();
    }

    public void expireActionOn(UUID handId, UUID playerId) {
        checkHandIsBeingPlayed();

        if (!currentHand.idMatches(handId)) {
            return;
        }

        TableEvent forcedActionOnExpiredEvent = currentHand.expireActionOn(playerId,
                ++aggregateVersion);
        addNewEvent(forcedActionOnExpiredEvent);
        applyCommonEvent(forcedActionOnExpiredEvent);

        handleEndOfRound();
    }

    public void autoMoveHandForward(UUID handId) {
        checkHandIsBeingPlayed();

        if (!currentHand.idMatches(handId)) {
            return;
        }

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
        List<TableEvent> endOfRoundEvents = currentHand
                .handlePotAndRoundCompleted(aggregateVersion + 1);
        endOfRoundEvents.forEach(x -> {
            addNewEvent(x);
            // TODO: not using applyCommonEvent() here because PotHandler is too
            // stateful and the events get applied to the state down there. when
            // that's refactored, this should change
            addAppliedEvent(x);
            aggregateVersion++;
        });
    }

    private void changeActionOnIfAppropriate() {
        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(aggregateVersion + 1);
        actionOnChangedEvents.forEach(x -> {
            addNewEvent(x);
            applyCommonEvent(x);
            aggregateVersion++;
        });
    }

    private void dealCommonCardsIfAppropriate() {
        currentHand.dealCommonCardsIfAppropriate(aggregateVersion + 1).ifPresent(
                event -> {
                    addNewEvent(event);
                    applyCommonEvent(event);
                    aggregateVersion++;
                });
    }

    private void determineWinnersIfAppropriate() {
        currentHand.determineWinnersIfAppropriate(aggregateVersion + 1).ifPresent(
                event -> {
                    addNewEvent(event);
                    applyCommonEvent(event);
                    aggregateVersion++;
                });
    }

    private void removeAnyBustedPlayers() {
        chipsInBack.entrySet().stream()
            .filter(x -> x.getValue() == 0)
            .forEach(x -> {
                PlayerBustedTableEvent event = new PlayerBustedTableEvent(aggregateId,
                        aggregateVersion, gameId, x.getKey());
                addNewEvent(event);
                applyCommonEvent(event);
                aggregateVersion++;
            });
    }

    private void finishHandIfAppropriate() {
        Optional<TableEvent> handCompleteEvent = currentHand.finishHandIfAppropriate(aggregateVersion + 1);

        if (handCompleteEvent.isPresent()) {
            addNewEvent(handCompleteEvent.get());
            applyCommonEvent(handCompleteEvent.get());
            aggregateVersion++;
        } else {
            currentHand.autoMoveHandForward(aggregateVersion + 1).ifPresent(event -> {
                addNewEvent(event);
                applyCommonEvent(event);
                aggregateVersion++;
            });
        }
    }

    public void addPlayer(UUID playerId, int chips) {
        if (seatMap.values().contains(playerId)) {
            throw new FlexPokerException("player already at this table");
        }

        int newPlayerPosition = findRandomOpenSeat();

        PlayerAddedEvent playerAddedEvent = new PlayerAddedEvent(aggregateId,
                ++aggregateVersion, gameId, playerId, chips, newPlayerPosition);
        addNewEvent(playerAddedEvent);
        applyCommonEvent(playerAddedEvent);
    }

    public void removePlayer(UUID playerId) {
        if (!seatMap.values().contains(playerId)) {
            throw new FlexPokerException("player not at this table");
        }

        if (currentHand != null) {
            throw new FlexPokerException("can't remove a player while in a hand");
        }

        PlayerRemovedEvent playerRemovedEvent = new PlayerRemovedEvent(aggregateId, ++aggregateVersion, gameId, playerId);
        addNewEvent(playerRemovedEvent);
        applyCommonEvent(playerRemovedEvent);
    }

    public void pause() {
        if (paused) {
            throw new FlexPokerException("table is already paused.  can't pause again.");
        }

        TablePausedEvent tablePausedEvent = new TablePausedEvent(aggregateId, ++aggregateVersion, gameId);
        addNewEvent(tablePausedEvent);
        applyCommonEvent(tablePausedEvent);
    }

    public void resume() {
        if (!paused) {
            throw new FlexPokerException("table is not paused.  can't resume.");
        }

        TableResumedEvent tableResumedEvent = new TableResumedEvent(aggregateId, ++aggregateVersion, gameId);
        addNewEvent(tableResumedEvent);
        applyCommonEvent(tableResumedEvent);
    }

    private int findRandomOpenSeat() {
        while (true) {
            int potentialNewPlayerPosition = new Random().nextInt(seatMap.size());
            if (seatMap.get(potentialNewPlayerPosition) == null) {
                return potentialNewPlayerPosition;
            }
        }
    }

}
