package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandDealerState;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.PlayerAction;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class Table extends AggregateRoot<TableEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final UUID gameId;

    private final Map<Integer, UUID> seatMap;

    private final Map<UUID, Integer> chipsInBack;

    private Hand currentHand;

    protected Table(UUID aggregateId, UUID gameId, Map<Integer, UUID> seatMap) {
        this.aggregateId = aggregateId;
        this.gameId = gameId;
        this.seatMap = seatMap;
        this.chipsInBack = new HashMap<>();
    }

    @Override
    public void applyAllEvents(List<TableEvent> events) {
        for (TableEvent event : events) {
            aggregateVersion++;
            switch (event.getType()) {
            case TableCreated:
                applyEvent((TableCreatedEvent) event);
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
            case ActionOnChanged:
                applyEvent((ActionOnChangedEvent) event);
                break;
            case LastToActChanged:
                applyEvent((LastToActChangedEvent) event);
                break;
            default:
                throw new IllegalArgumentException("Event Type cannot be handled: "
                        + event.getType());
            }
        }
    }

    private void applyEvent(TableCreatedEvent event) {
        seatMap.putAll(event.getSeatPositionToPlayerMap());
        event.getSeatPositionToPlayerMap()
                .values()
                .stream()
                .filter(x -> x != null)
                .forEach(
                        x -> chipsInBack.put(x,
                                Integer.valueOf(event.getStartingNumberOfChips())));
    }

    private void applyEvent(HandDealtEvent event) {
        currentHand = new Hand(event.getGameId(), event.getAggregateId(),
                event.getHandId(), seatMap, event.getFlopCards(), event.getTurnCard(),
                event.getRiverCard(), event.getButtonOnPosition(),
                event.getSmallBlindPosition(), event.getBigBlindPosition(),
                event.getActionOnPosition(), event.getLastToActPlayerId(),
                event.getPlayerToPocketCardsMap(), event.getPossibleSeatActionsMap(),
                event.getPlayersStillInHand(), event.getHandEvaluations(),
                event.getPots(), event.getHandDealerState(), event.getChipsInBack(),
                event.getChipsInFrontMap(), event.getCallAmountsMap(),
                event.getRaiseToAmountsMap(), event.getBlinds(),
                event.getPlayersToShowCardsMap());
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

    public void createNewTable(Set<UUID> playerIds) {
        if (!seatMap.values().stream().allMatch(x -> x == null)) {
            throw new FlexPokerException("seatMap already contains players");
        }

        if (playerIds.size() < 2) {
            throw new FlexPokerException("must have at least two players");
        }

        if (playerIds.size() > seatMap.size()) {
            throw new FlexPokerException("player list can't be larger than seatMap");
        }

        if (currentHand != null) {
            throw new FlexPokerException(
                    "can't create a table that already has a hand played");
        }

        Map<Integer, UUID> seatToPlayerMap = new HashMap<>();
        // TODO: randomize the seat placement
        List<UUID> playerIdsList = new ArrayList<>(playerIds);
        for (int i = 0; i < playerIds.size(); i++) {
            seatToPlayerMap.put(Integer.valueOf(i), playerIdsList.get(i));
        }

        TableCreatedEvent tableCreatedEvent = new TableCreatedEvent(aggregateId,
                ++aggregateVersion, gameId, seatMap.size(), seatToPlayerMap, 1500);
        addNewEvent(tableCreatedEvent);
        applyEvent(tableCreatedEvent);
    }

    public void startNewHandForNewGame(Blinds blinds, List<Card> shuffledDeckOfCards,
            CardsUsedInHand cardsUsedInHand,
            Map<PocketCards, HandEvaluation> handEvaluations) {

        CardsShuffledEvent cardsShuffledEvent = new CardsShuffledEvent(aggregateId,
                ++aggregateVersion, gameId, shuffledDeckOfCards);
        addNewEvent(cardsShuffledEvent);

        int buttonOnPosition = assignButtonOnForNewGame();
        int smallBlindPosition = assignSmallBlindForNewGame(buttonOnPosition);
        int bigBlindPosition = assignBigBlindForNewGame(buttonOnPosition,
                smallBlindPosition);
        int actionOnPosition = assignActionOnForNewGame(bigBlindPosition);

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
                bigBlindPosition, actionOnPosition, null, playerToPocketCardsMap,
                possibleSeatActionsMap, playersStillInHand, new ArrayList<>(
                        handEvaluations.values()), new HashSet<>(), HandDealerState.NONE,
                chipsInBack, new HashMap<>(), new HashMap<>(), new HashMap<>(), blinds,
                new HashSet<>());
        HandDealtEvent handDealtEvent = hand.dealHand(++aggregateVersion);
        addNewEvent(handDealtEvent);
        applyEvent(handDealtEvent);
    }

    private int assignButtonOnForNewGame() {
        while (true) {
            int potentialButtonOnPosition = new Random().nextInt(seatMap.size());
            if (seatMap.get(Integer.valueOf(potentialButtonOnPosition)) != null) {
                return potentialButtonOnPosition;
            }
        }
    }

    private int assignSmallBlindForNewGame(int buttonOnPosition) {
        return getNumberOfPlayersAtTable() == 2 ? buttonOnPosition
                : findNextFilledSeat(buttonOnPosition);
    }

    private int assignBigBlindForNewGame(int buttonOnPosition, int smallBlindPosition) {
        return getNumberOfPlayersAtTable() == 2 ? findNextFilledSeat(buttonOnPosition)
                : findNextFilledSeat(smallBlindPosition);
    }

    private int assignActionOnForNewGame(int bigBlindPosition) {
        return findNextFilledSeat(bigBlindPosition);
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

    public int getNumberOfPlayersAtTable() {
        return (int) seatMap.values().stream().filter(x -> x != null).count();
    }

    public void check(UUID playerId) {
        checkHandIsBeingPlayed();

        PlayerCheckedEvent playerCheckedEvent = currentHand.check(playerId,
                ++aggregateVersion);
        addNewEvent(playerCheckedEvent);
        applyEvent(playerCheckedEvent);

        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(++aggregateVersion);
        actionOnChangedEvents.forEach(x -> addNewEvent(x));
        applyAllEvents(actionOnChangedEvents);

        currentHand.dealCommonCardsIfAppropriate(++aggregateVersion).ifPresent(event -> {
            addNewEvent(event);
            applyAllEvents(Arrays.asList(event));
        });
    }

    public void call(UUID playerId) {
        checkHandIsBeingPlayed();

        PlayerCalledEvent playerCalledEvent = currentHand.call(playerId,
                ++aggregateVersion);
        addNewEvent(playerCalledEvent);
        applyEvent(playerCalledEvent);

        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(++aggregateVersion);
        actionOnChangedEvents.forEach(x -> addNewEvent(x));
        applyAllEvents(actionOnChangedEvents);

        currentHand.dealCommonCardsIfAppropriate(++aggregateVersion).ifPresent(event -> {
            addNewEvent(event);
            applyAllEvents(Arrays.asList(event));
        });
    }

    public void fold(UUID playerId) {
        checkHandIsBeingPlayed();

        PlayerFoldedEvent playerFoldedEvent = currentHand.fold(playerId,
                ++aggregateVersion);
        addNewEvent(playerFoldedEvent);
        applyEvent(playerFoldedEvent);

        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(++aggregateVersion);
        actionOnChangedEvents.forEach(x -> addNewEvent(x));
        applyAllEvents(actionOnChangedEvents);

        currentHand.dealCommonCardsIfAppropriate(++aggregateVersion).ifPresent(event -> {
            addNewEvent(event);
            applyAllEvents(Arrays.asList(event));
        });
    }

    public void raise(UUID playerId, int raiseToAmount) {
        checkHandIsBeingPlayed();

        PlayerRaisedEvent playerRaisedEvent = currentHand.raise(playerId,
                ++aggregateVersion, raiseToAmount);
        addNewEvent(playerRaisedEvent);
        applyEvent(playerRaisedEvent);

        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(++aggregateVersion);
        actionOnChangedEvents.forEach(x -> addNewEvent(x));
        applyAllEvents(actionOnChangedEvents);
    }

    public void expireActionOn(UUID playerId) {
        checkHandIsBeingPlayed();

        // TODO: player folding/checking is different than being forced. two
        // different events should be used
        TableEvent forcedActionOnExpiredEvent = currentHand.expireActionOn(playerId,
                ++aggregateVersion);
        addNewEvent(forcedActionOnExpiredEvent);
        applyAllEvents(Arrays.asList(forcedActionOnExpiredEvent));

        List<TableEvent> actionOnChangedEvents = currentHand
                .changeActionOn(++aggregateVersion);
        actionOnChangedEvents.forEach(x -> addNewEvent(x));
        applyAllEvents(actionOnChangedEvents);

        currentHand.dealCommonCardsIfAppropriate(++aggregateVersion).ifPresent(event -> {
            addNewEvent(event);
            applyAllEvents(Arrays.asList(event));
        });
    }

    private void checkHandIsBeingPlayed() {
        if (currentHand == null) {
            throw new FlexPokerException("no hand in progress");
        }
    }

}
