package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class Table extends AggregateRoot<TableEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final UUID gameId;

    private final Map<Integer, UUID> seatMap;

    protected Table(UUID aggregateId, UUID gameId, Map<Integer, UUID> seatMap) {
        this.aggregateId = aggregateId;
        this.gameId = gameId;
        this.seatMap = seatMap;
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
            default:
                throw new IllegalArgumentException("Event Type cannot be handled: "
                        + event.getType());
            }
        }
    }

    private void applyEvent(TableCreatedEvent event) {
        seatMap.putAll(event.getSeatPositionToPlayerMap());
    }

    private void applyEvent(HandDealtEvent event) {
        // TODO: nothing to do for now
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

        Map<Integer, UUID> seatToPlayerMap = new HashMap<>();
        // TODO: randomize the seat placement
        List<UUID> playerIdsList = new ArrayList<>(playerIds);
        for (int i = 0; i < playerIds.size(); i++) {
            seatToPlayerMap.put(i, playerIdsList.get(i));
        }

        TableCreatedEvent tableCreatedEvent = new TableCreatedEvent(aggregateId,
                ++aggregateVersion, gameId, seatMap.size(), seatToPlayerMap, 1500);
        addNewEvent(tableCreatedEvent);
        applyEvent(tableCreatedEvent);
    }

    public void startNewHandForNewGame(List<Card> shuffledDeckOfCards,
            CardsUsedInHand cardsUsedInHand) {

        // TODO: this stuff
        // setSeatStatusForNewGameCommand.execute(game, table);
        // startNewHandCommand.execute(game, table);

        CardsShuffledEvent cardsShuffledEvent = new CardsShuffledEvent(aggregateId,
                ++aggregateVersion, gameId, shuffledDeckOfCards);
        addNewEvent(cardsShuffledEvent);

        int buttonOnPosition = assignButtonOnForNewGame();
        int smallBlindPosition = assignSmallBlindForNewGame(buttonOnPosition);
        int bigBlindPosition = assignBigBlindForNewGame(buttonOnPosition,
                smallBlindPosition);
        int actionOnPosition = assignActionOnForNewGame(bigBlindPosition);

        int nextToReceivePocketCards = findNextFilledSeat(buttonOnPosition);
        Map<Integer, PocketCards> positionToPocketCardsMap = new HashMap<>();

        for (PocketCards pocketCards : cardsUsedInHand.getPocketCards()) {
            positionToPocketCardsMap.put(Integer.valueOf(nextToReceivePocketCards),
                    pocketCards);
            nextToReceivePocketCards = findNextFilledSeat(nextToReceivePocketCards);
        }

        HandDealtEvent handDealtEvent = new HandDealtEvent(aggregateId,
                ++aggregateVersion, gameId, cardsUsedInHand.getFlopCards(),
                cardsUsedInHand.getTurnCard(), cardsUsedInHand.getRiverCard(),
                buttonOnPosition, smallBlindPosition, bigBlindPosition, actionOnPosition,
                positionToPocketCardsMap);
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

}
