package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.RoundCompletedEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class Table extends AggregateRoot<TableEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final UUID gameId;

    private final Map<Integer, UUID> seatMap;

    private final Map<UUID, Integer> chipsInBack;

    private int buttonOnPosition;

    private int smallBlindPosition;

    private int bigBlindPosition;

    private Hand currentHand;

    protected Table(boolean creatingFromEvents, UUID aggregateId, UUID gameId,
            Map<Integer, UUID> seatMap, int startingNumberOfChips) {
        this.aggregateId = aggregateId;
        this.gameId = gameId;
        this.seatMap = seatMap;
        this.chipsInBack = new HashMap<>();

        seatMap.values().stream().filter(x -> x != null)
                .forEach(x -> chipsInBack.put(x, startingNumberOfChips));

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

    private void applyCommonEvent(TableEvent event) {
        switch (event.getType()) {
        case TableCreated:
            break;
        case CardsShuffled:
            break;
        case HandDealtEvent:
            applyEvent((HandDealtEvent) event);
            break;
        case PlayerCalled:
            applyEvent((PlayerCalledEvent) event);
            break;
        case PlayerChecked:
            applyEvent((PlayerCheckedEvent) event);
            break;
        case PlayerFolded:
            applyEvent((PlayerFoldedEvent) event);
            break;
        case PlayerRaised:
            applyEvent((PlayerRaisedEvent) event);
            break;
        case FlopCardsDealt:
            applyEvent((FlopCardsDealtEvent) event);
            break;
        case TurnCardDealt:
            applyEvent((TurnCardDealtEvent) event);
            break;
        case RiverCardDealt:
            applyEvent((RiverCardDealtEvent) event);
            break;
        case PotAmountIncreased:
            applyEvent((PotAmountIncreasedEvent) event);
            break;
        case PotClosed:
            applyEvent((PotClosedEvent) event);
            break;
        case PotCreated:
            applyEvent((PotCreatedEvent) event);
            break;
        case RoundCompleted:
            applyEvent((RoundCompletedEvent) event);
            break;
        case ActionOnChanged:
            applyEvent((ActionOnChangedEvent) event);
            break;
        case LastToActChanged:
            applyEvent((LastToActChangedEvent) event);
            break;
        case WinnersDetermined:
            applyEvent((WinnersDeterminedEvent) event);
            break;
        case HandCompleted:
            applyEvent((HandCompletedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
        addAppliedEvent(event);
    }

    private void applyEvent(HandDealtEvent event) {
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
                event.getSmallBlind(), event.getBigBlind(),
                event.getPlayersToShowCards());
    }

    private void applyEvent(PlayerRaisedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(PlayerFoldedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(PlayerCheckedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(PlayerCalledEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(ActionOnChangedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(LastToActChangedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(RiverCardDealtEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(TurnCardDealtEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(FlopCardsDealtEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(PotAmountIncreasedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(PotClosedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(PotCreatedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(RoundCompletedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(WinnersDeterminedEvent event) {
        currentHand.applyEvent(event);
    }

    private void applyEvent(@SuppressWarnings("unused") HandCompletedEvent event) {
        currentHand = null;
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
                new HashMap<>(), smallBlind, bigBlind, new HashSet<>());
        List<TableEvent> eventsCreated = hand.dealHand(++aggregateVersion,
                actionOnPosition);
        eventsCreated.forEach(x -> {
            addNewEvent(x);
            applyCommonEvent(x);
        });

        aggregateVersion += eventsCreated.size() - 1;
    }

    public int getNumberOfPlayersAtTable() {
        return (int) seatMap.values().stream().filter(x -> x != null).count();
    }

    public void check(UUID playerId) {
        checkHandIsBeingPlayed();

        PlayerCheckedEvent playerCheckedEvent = currentHand.check(playerId,
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

        PlayerFoldedEvent playerFoldedEvent = currentHand.fold(playerId,
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

        // TODO: player folding/checking is different than being forced. two
        // different events should be used
        TableEvent forcedActionOnExpiredEvent = currentHand.expireActionOn(playerId,
                ++aggregateVersion);
        addNewEvent(forcedActionOnExpiredEvent);
        applyCommonEvent(forcedActionOnExpiredEvent);

        handleEndOfRound();
    }

    private void handleEndOfRound() {
        handlePotAndRoundCompleted();
        changeActionOnIfAppropriate();
        dealCommonCardsIfAppropriate();
        determineWinnersIfAppropriate();
        finishHandIfAppropriate();
    }

    private void checkHandIsBeingPlayed() {
        if (currentHand == null) {
            throw new FlexPokerException("no hand in progress");
        }
    }

    private void handlePotAndRoundCompleted() {
        List<TableEvent> endOfRoundEvents = currentHand
                .handlePotAndRoundCompleted(++aggregateVersion);
        endOfRoundEvents.forEach(x -> addNewEvent(x));
        aggregateVersion += endOfRoundEvents.size() - 1;
    }

    private void changeActionOnIfAppropriate() {
        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(++aggregateVersion);
        actionOnChangedEvents.forEach(x -> {
            addNewEvent(x);
            applyCommonEvent(x);
        });
        aggregateVersion += actionOnChangedEvents.size() - 1;
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

    private void finishHandIfAppropriate() {
        currentHand.finishHandIfAppropriate(aggregateVersion + 1).ifPresent(event -> {
            addNewEvent(event);
            applyCommonEvent(event);
            aggregateVersion++;
        });
    }

}
