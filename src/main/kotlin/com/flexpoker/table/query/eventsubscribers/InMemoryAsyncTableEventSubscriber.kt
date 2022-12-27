package com.flexpoker.table.query.eventsubscribers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.event.subscriber.EventSubscriber
import com.flexpoker.framework.event.subscriber.InMemoryThreadSafeEventSubscriberHelper
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerAddedEvent
import com.flexpoker.table.command.events.PlayerBustedTableEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PlayerForceCheckedEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.command.events.PlayerRemovedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TablePausedEvent
import com.flexpoker.table.command.events.TableResumedEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component("tableEventSubscriber")
class InMemoryAsyncTableEventSubscriber @Inject constructor(
    private val inMemoryThreadSafeEventSubscriberHelper: InMemoryThreadSafeEventSubscriberHelper<TableEvent>,
    private val tableCreatedEventHandler: EventHandler<TableCreatedEvent>,
    private val handDealtEventHandler: EventHandler<HandDealtEvent>,
    private val playerBustedTableEventHandler: EventHandler<PlayerBustedTableEvent>,
    private val playerCalledEventHandler: EventHandler<PlayerCalledEvent>,
    private val playerCheckedEventHandler: EventHandler<PlayerCheckedEvent>,
    private val playerForceCheckedEventHandler: EventHandler<PlayerForceCheckedEvent>,
    private val playerFoldedEventHandler: EventHandler<PlayerFoldedEvent>,
    private val playerForceFoldedEventHandler: EventHandler<PlayerForceFoldedEvent>,
    private val playerRaisedEventHandler: EventHandler<PlayerRaisedEvent>,
    private val flopCardsDealtEventHandler: EventHandler<FlopCardsDealtEvent>,
    private val turnCardDealtEventHandler: EventHandler<TurnCardDealtEvent>,
    private val riverCardDealtEventHandler: EventHandler<RiverCardDealtEvent>,
    private val roundCompletedEventHandler: EventHandler<RoundCompletedEvent>,
    private val actionOnChangedEventHandler: EventHandler<ActionOnChangedEvent>,
    private val potAmountIncreasedEventHandler: EventHandler<PotAmountIncreasedEvent>,
    private val potClosedEventHandler: EventHandler<PotClosedEvent>,
    private val potCreatedEventHandler: EventHandler<PotCreatedEvent>,
    private val winnersDeterminedEventHandler: EventHandler<WinnersDeterminedEvent>
) : EventSubscriber<TableEvent> {

    companion object {
        private val NOOP = EventHandler { _: TableEvent -> }
    }

    init {
        inMemoryThreadSafeEventSubscriberHelper.setHandlerMap(createEventHandlerMap()
                 as MutableMap<Class<TableEvent>, EventHandler<TableEvent>>)
    }

    @Async
    override fun receive(event: TableEvent) {
        inMemoryThreadSafeEventSubscriberHelper.receive(event)
    }

    private fun createEventHandlerMap(): Map<Class<out TableEvent>, EventHandler<out TableEvent>> {
        val eventHandlerMap = HashMap<Class<out TableEvent>, EventHandler<out TableEvent>>()
        eventHandlerMap[ActionOnChangedEvent::class.java] = actionOnChangedEventHandler
        eventHandlerMap[AutoMoveHandForwardEvent::class.java] = NOOP
        eventHandlerMap[CardsShuffledEvent::class.java] = NOOP
        eventHandlerMap[FlopCardsDealtEvent::class.java] = flopCardsDealtEventHandler
        eventHandlerMap[HandCompletedEvent::class.java] = NOOP
        eventHandlerMap[HandDealtEvent::class.java] = handDealtEventHandler
        eventHandlerMap[LastToActChangedEvent::class.java] = NOOP
        eventHandlerMap[PlayerAddedEvent::class.java] = NOOP
        eventHandlerMap[PlayerBustedTableEvent::class.java] = playerBustedTableEventHandler
        eventHandlerMap[PlayerCalledEvent::class.java] = playerCalledEventHandler
        eventHandlerMap[PlayerCheckedEvent::class.java] = playerCheckedEventHandler
        eventHandlerMap[PlayerForceCheckedEvent::class.java] = playerForceCheckedEventHandler
        eventHandlerMap[PlayerFoldedEvent::class.java] = playerFoldedEventHandler
        eventHandlerMap[PlayerForceFoldedEvent::class.java] = playerForceFoldedEventHandler
        eventHandlerMap[PlayerRaisedEvent::class.java] = playerRaisedEventHandler
        eventHandlerMap[PlayerRemovedEvent::class.java] = NOOP
        eventHandlerMap[PotAmountIncreasedEvent::class.java] = potAmountIncreasedEventHandler
        eventHandlerMap[PotClosedEvent::class.java] = potClosedEventHandler
        eventHandlerMap[PotCreatedEvent::class.java] = potCreatedEventHandler
        eventHandlerMap[RiverCardDealtEvent::class.java] = riverCardDealtEventHandler
        eventHandlerMap[RoundCompletedEvent::class.java] = roundCompletedEventHandler
        eventHandlerMap[TableCreatedEvent::class.java] = tableCreatedEventHandler
        eventHandlerMap[TablePausedEvent::class.java] = NOOP
        eventHandlerMap[TableResumedEvent::class.java] = NOOP
        eventHandlerMap[TurnCardDealtEvent::class.java] = turnCardDealtEventHandler
        eventHandlerMap[WinnersDeterminedEvent::class.java] = winnersDeterminedEventHandler
        return eventHandlerMap
    }

}