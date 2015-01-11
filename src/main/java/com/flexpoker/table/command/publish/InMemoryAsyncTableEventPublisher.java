package com.flexpoker.table.command.publish;

import javax.inject.Inject;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.FlopCardsDealtEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.PlayerCalledEvent;
import com.flexpoker.table.command.events.PlayerCheckedEvent;
import com.flexpoker.table.command.events.PlayerFoldedEvent;
import com.flexpoker.table.command.events.PlayerRaisedEvent;
import com.flexpoker.table.command.events.RiverCardDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.events.TurnCardDealtEvent;
import com.flexpoker.table.command.framework.TableEventType;

@Component
public class InMemoryAsyncTableEventPublisher implements EventPublisher<TableEventType> {

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

    private final ProcessManager<ActionOnChangedEvent> actionOnCountdownProcessManager;

    private final ProcessManager<HandCompletedEvent> attemptToStartNewHandForExistingTableProcessManager;

    @Inject
    @Lazy
    public InMemoryAsyncTableEventPublisher(
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
            ProcessManager<ActionOnChangedEvent> actionOnCountdownProcessManager,
            ProcessManager<HandCompletedEvent> attemptToStartNewHandForExistingTableProcessManager) {
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
        this.actionOnCountdownProcessManager = actionOnCountdownProcessManager;
        this.attemptToStartNewHandForExistingTableProcessManager = attemptToStartNewHandForExistingTableProcessManager;
    }

    @Override
    public void publish(Event<TableEventType> event) {
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
            actionOnCountdownProcessManager.handle((ActionOnChangedEvent) event);
            break;
        case LastToActChanged:
            break;
        case HandCompleted:
            attemptToStartNewHandForExistingTableProcessManager
                    .handle((HandCompletedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }
}
