package com.flexpoker.table.query.eventsubscribers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.PotAmountIncreasedEvent;
import com.flexpoker.table.command.events.PotClosedEvent;
import com.flexpoker.table.command.events.PotCreatedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.events.WinnersDeterminedEvent;
import com.flexpoker.table.command.framework.TableEvent;

@Component("tableEventSubscriber")
public class InMemoryAsyncTableEventSubscriber implements EventSubscriber<TableEvent> {

    private final Map<UUID, List<TableEvent>> listOfTableEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private final EventHandler<TableCreatedEvent> tableCreatedEventHandler;

    private final EventHandler<HandDealtEvent> handDealtEventHandler;

    private final EventHandler<PlayerCalledEvent> playerCalledEventHandler;

    private final EventHandler<PlayerCheckedEvent> playerCheckedEventHandler;

    private final EventHandler<PlayerFoldedEvent> playerFoldedEventHandler;

    private final EventHandler<PlayerRaisedEvent> playerRaisedEventHandler;

    private final EventHandler<FlopCardsDealtEvent> flopCardsDealtEventHandler;

    private final EventHandler<TurnCardDealtEvent> turnCardDealtEventHandler;

    private final EventHandler<RiverCardDealtEvent> riverCardDealtEventHandler;

    private final EventHandler<ActionOnChangedEvent> actionOnChangedEventHandler;

    private final EventHandler<PotAmountIncreasedEvent> potAmountIncreasedEventHandler;

    private final EventHandler<PotClosedEvent> potClosedEventHandler;

    private final EventHandler<PotCreatedEvent> potCreatedEventHandler;

    private final EventHandler<WinnersDeterminedEvent> winnersDeterminedEventHandler;

    @Inject
    public InMemoryAsyncTableEventSubscriber(
            EventHandler<TableCreatedEvent> tableCreatedEventHandler,
            EventHandler<HandDealtEvent> handDealtEventHandler,
            EventHandler<PlayerCalledEvent> playerCalledEventHandler,
            EventHandler<PlayerCheckedEvent> playerCheckedEventHandler,
            EventHandler<PlayerFoldedEvent> playerFoldedEventHandler,
            EventHandler<PlayerRaisedEvent> playerRaisedEventHandler,
            EventHandler<FlopCardsDealtEvent> flopCardsDealtEventHandler,
            EventHandler<TurnCardDealtEvent> turnCardDealtEventHandler,
            EventHandler<RiverCardDealtEvent> riverCardDealtEventHandler,
            EventHandler<ActionOnChangedEvent> actionOnChangedEventHandler,
            EventHandler<PotAmountIncreasedEvent> potAmountIncreasedEventHandler,
            EventHandler<PotClosedEvent> potClosedEventHandler,
            EventHandler<PotCreatedEvent> potCreatedEventHandler,
            EventHandler<WinnersDeterminedEvent> winnersDeterminedEventHandler) {
        listOfTableEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
        this.tableCreatedEventHandler = tableCreatedEventHandler;
        this.handDealtEventHandler = handDealtEventHandler;
        this.playerCalledEventHandler = playerCalledEventHandler;
        this.playerCheckedEventHandler = playerCheckedEventHandler;
        this.playerFoldedEventHandler = playerFoldedEventHandler;
        this.playerRaisedEventHandler = playerRaisedEventHandler;
        this.flopCardsDealtEventHandler = flopCardsDealtEventHandler;
        this.turnCardDealtEventHandler = turnCardDealtEventHandler;
        this.riverCardDealtEventHandler = riverCardDealtEventHandler;
        this.actionOnChangedEventHandler = actionOnChangedEventHandler;
        this.potAmountIncreasedEventHandler = potAmountIncreasedEventHandler;
        this.potClosedEventHandler = potClosedEventHandler;
        this.potCreatedEventHandler = potCreatedEventHandler;
        this.winnersDeterminedEventHandler = winnersDeterminedEventHandler;
    }

    @Async
    @Override
    public void receive(TableEvent event) {
        listOfTableEventsNeededToProcess.putIfAbsent(event.getAggregateId(),
                new CopyOnWriteArrayList<>());
        nextExpectedEventVersion.putIfAbsent(event.getAggregateId(), Integer.valueOf(1));

        if (isExpectedEvent(event)) {
            handleEventAndRunAnyOthers(event);
        } else {
            listOfTableEventsNeededToProcess.get(event.getAggregateId()).add(event);
        }
    }

    private void handleEventAndRunAnyOthers(TableEvent event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(TableEvent event) {
        int expectedEventVersion = nextExpectedEventVersion.get(event.getAggregateId())
                .intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(TableEvent event) {
        if (event.getClass() == TableCreatedEvent.class) {
            tableCreatedEventHandler.handle((TableCreatedEvent) event);
        } else if (event.getClass() == HandDealtEvent.class) {
            handDealtEventHandler.handle((HandDealtEvent) event);
        } else if (event.getClass() == PlayerCalledEvent.class) {
            playerCalledEventHandler.handle((PlayerCalledEvent) event);
        } else if (event.getClass() == PlayerCheckedEvent.class) {
            playerCheckedEventHandler.handle((PlayerCheckedEvent) event);
        } else if (event.getClass() == PlayerFoldedEvent.class) {
            playerFoldedEventHandler.handle((PlayerFoldedEvent) event);
        } else if (event.getClass() == PlayerRaisedEvent.class) {
            playerRaisedEventHandler.handle((PlayerRaisedEvent) event);
        } else if (event.getClass() == FlopCardsDealtEvent.class) {
            flopCardsDealtEventHandler.handle((FlopCardsDealtEvent) event);
        } else if (event.getClass() == TurnCardDealtEvent.class) {
            turnCardDealtEventHandler.handle((TurnCardDealtEvent) event);
        } else if (event.getClass() == RiverCardDealtEvent.class) {
            riverCardDealtEventHandler.handle((RiverCardDealtEvent) event);
        } else if (event.getClass() == ActionOnChangedEvent.class) {
            actionOnChangedEventHandler.handle((ActionOnChangedEvent) event);
        } else if (event.getClass() == PotAmountIncreasedEvent.class) {
            potAmountIncreasedEventHandler.handle((PotAmountIncreasedEvent) event);
        } else if (event.getClass() == PotClosedEvent.class) {
            potClosedEventHandler.handle((PotClosedEvent) event);
        } else if (event.getClass() == PotCreatedEvent.class) {
            potCreatedEventHandler.handle((PotCreatedEvent) event);
        } else if (event.getClass() == WinnersDeterminedEvent.class) {
            winnersDeterminedEventHandler.handle((WinnersDeterminedEvent) event);
        }
    }

    private void removeEventFromUnhandleList(TableEvent event) {
        listOfTableEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(TableEvent event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(TableEvent event) {
        List<TableEvent> unHandledEvents = new ArrayList<>(
                listOfTableEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
