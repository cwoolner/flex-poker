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

import com.flexpoker.framework.event.Event;
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
import com.flexpoker.table.command.framework.TableEventType;

@Component
public class InMemoryAsyncTableEventSubscriber implements EventSubscriber<TableEventType> {

    private final Map<UUID, List<Event<TableEventType>>> listOfTableEventsNeededToProcess;

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
    public void receive(Event<TableEventType> event) {
        listOfTableEventsNeededToProcess.putIfAbsent(event.getAggregateId(),
                new CopyOnWriteArrayList<>());
        nextExpectedEventVersion.putIfAbsent(event.getAggregateId(), Integer.valueOf(1));

        if (isExpectedEvent(event)) {
            handleEventAndRunAnyOthers(event);
        } else {
            listOfTableEventsNeededToProcess.get(event.getAggregateId()).add(event);
        }
    }

    private void handleEventAndRunAnyOthers(Event<TableEventType> event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(Event<TableEventType> event) {
        int expectedEventVersion = nextExpectedEventVersion.get(event.getAggregateId())
                .intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(Event<TableEventType> event) {
        switch (event.getType()) {
        case TableCreated:
            tableCreatedEventHandler.handle((TableCreatedEvent) event);
            break;
        case CardsShuffled:
            break;
        case HandDealtEvent:
            handDealtEventHandler.handle((HandDealtEvent) event);
            break;
        case PlayerCalled:
            playerCalledEventHandler.handle((PlayerCalledEvent) event);
            break;
        case PlayerChecked:
            playerCheckedEventHandler.handle((PlayerCheckedEvent) event);
            break;
        case PlayerFolded:
            playerFoldedEventHandler.handle((PlayerFoldedEvent) event);
            break;
        case PlayerRaised:
            playerRaisedEventHandler.handle((PlayerRaisedEvent) event);
            break;
        case FlopCardsDealt:
            flopCardsDealtEventHandler.handle((FlopCardsDealtEvent) event);
            break;
        case TurnCardDealt:
            turnCardDealtEventHandler.handle((TurnCardDealtEvent) event);
            break;
        case RiverCardDealt:
            riverCardDealtEventHandler.handle((RiverCardDealtEvent) event);
            break;
        case ActionOnChanged:
            actionOnChangedEventHandler.handle((ActionOnChangedEvent) event);
            break;
        case LastToActChanged:
            break;
        case PotAmountIncreased:
            potAmountIncreasedEventHandler.handle((PotAmountIncreasedEvent) event);
            break;
        case PotClosed:
            potClosedEventHandler.handle((PotClosedEvent) event);
            break;
        case PotCreated:
            potCreatedEventHandler.handle((PotCreatedEvent) event);
            break;
        case RoundCompleted:
            break;
        case WinnersDetermined:
            winnersDeterminedEventHandler.handle((WinnersDeterminedEvent) event);
            break;
        case HandCompleted:
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }

    private void removeEventFromUnhandleList(Event<TableEventType> event) {
        listOfTableEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(Event<TableEventType> event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(Event<TableEventType> event) {
        List<Event<TableEventType>> unHandledEvents = new ArrayList<>(
                listOfTableEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
