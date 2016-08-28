package com.flexpoker.table.query.eventsubscribers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.subscriber.EventSubscriber;
import com.flexpoker.framework.event.subscriber.InMemoryThreadSafeEventSubscriberHelper;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.CardsShuffledEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.LastToActChangedEvent;
import com.flexpoker.table.command.events.PlayerAddedEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
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

@Component("tableEventSubscriber")
public class InMemoryAsyncTableEventSubscriber implements EventSubscriber<TableEvent> {

    private final InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper;

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
            InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper,
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
        this.inMemoryThreadSafeEventSubscriberHelper = inMemoryThreadSafeEventSubscriberHelper;
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
        this.inMemoryThreadSafeEventSubscriberHelper.setHandlerMap(createEventHandlerMap());
    }

    @Async
    @Override
    public void receive(TableEvent event) {
        inMemoryThreadSafeEventSubscriberHelper.receive(event);
    }

    private Map<Class<? extends Event>, EventHandler<? extends Event>> createEventHandlerMap() {
        Map<Class<? extends Event>, EventHandler<? extends Event>> eventHandlerMap = new HashMap<>();
        eventHandlerMap.put(ActionOnChangedEvent.class, actionOnChangedEventHandler);
        eventHandlerMap.put(CardsShuffledEvent.class, x -> {});
        eventHandlerMap.put(FlopCardsDealtEvent.class, flopCardsDealtEventHandler);
        eventHandlerMap.put(HandCompletedEvent.class, x -> {});
        eventHandlerMap.put(HandDealtEvent.class, handDealtEventHandler);
        eventHandlerMap.put(LastToActChangedEvent.class, x -> {});
        eventHandlerMap.put(PlayerAddedEvent.class, x -> {});
        eventHandlerMap.put(PlayerCalledEvent.class, playerCalledEventHandler);
        eventHandlerMap.put(PlayerCheckedEvent.class, playerCheckedEventHandler);
        eventHandlerMap.put(PlayerFoldedEvent.class, playerFoldedEventHandler);
        eventHandlerMap.put(PlayerRaisedEvent.class, playerRaisedEventHandler);
        eventHandlerMap.put(PlayerRemovedEvent.class, x -> {});
        eventHandlerMap.put(PotAmountIncreasedEvent.class, potAmountIncreasedEventHandler);
        eventHandlerMap.put(PotClosedEvent.class, potClosedEventHandler);
        eventHandlerMap.put(PotCreatedEvent.class, potCreatedEventHandler);
        eventHandlerMap.put(RiverCardDealtEvent.class, riverCardDealtEventHandler);
        eventHandlerMap.put(RoundCompletedEvent.class, x -> {});
        eventHandlerMap.put(TableCreatedEvent.class, tableCreatedEventHandler);
        eventHandlerMap.put(TablePausedEvent.class, x -> {});
        eventHandlerMap.put(TableResumedEvent.class, x -> {});
        eventHandlerMap.put(TurnCardDealtEvent.class, turnCardDealtEventHandler);
        eventHandlerMap.put(WinnersDeterminedEvent.class, winnersDeterminedEventHandler);
        return eventHandlerMap;
    }

}
