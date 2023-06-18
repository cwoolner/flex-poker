package com.flexpoker.game.query.eventsubscribers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.event.subscriber.EventSubscriber
import com.flexpoker.framework.event.subscriber.InMemoryThreadSafeEventSubscriberHelper
import com.flexpoker.game.command.events.BlindsIncreasedEvent
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameFinishedEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import com.flexpoker.game.command.events.TableRemovedEvent
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import com.flexpoker.game.query.handlers.BlindsIncreasedEventHandler
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@Component("gameEventSubscriber")
class InMemoryAsyncGameEventSubscriber @Inject constructor(
    private val inMemoryThreadSafeEventSubscriberHelper: InMemoryThreadSafeEventSubscriberHelper<GameEvent>,
    private val blindsIncreasedEventHandler: BlindsIncreasedEventHandler,
    private val gameCreatedEventHandler: EventHandler<GameCreatedEvent>,
    private val gameJoinedEventHandler: EventHandler<GameJoinedEvent>,
    private val gameMovedToStartingStageEventHandler: EventHandler<GameMovedToStartingStageEvent>,
    private val gameStartedEventHandler: EventHandler<GameStartedEvent>,
    private val playerBustedGameEventHandler: EventHandler<PlayerBustedGameEvent>
) : EventSubscriber<GameEvent> {

    companion object {
        private val NOOP = EventHandler { _: GameEvent -> }
    }

    init {
        inMemoryThreadSafeEventSubscriberHelper.setHandlerMap(createEventHandlerMap()
                as MutableMap<Class<GameEvent>, EventHandler<GameEvent>>)
    }

    @Async
    override fun receive(event: GameEvent) {
        inMemoryThreadSafeEventSubscriberHelper.receive(event)
    }

    private fun createEventHandlerMap(): Map<Class<out GameEvent>, EventHandler<out GameEvent>> {
        val eventHandlerMap = HashMap<Class<out GameEvent>, EventHandler<out GameEvent>>()
        eventHandlerMap[BlindsIncreasedEvent::class.java] = blindsIncreasedEventHandler
        eventHandlerMap[GameCreatedEvent::class.java] = gameCreatedEventHandler
        eventHandlerMap[GameFinishedEvent::class.java] = NOOP
        eventHandlerMap[GameJoinedEvent::class.java] = gameJoinedEventHandler
        eventHandlerMap[GameMovedToStartingStageEvent::class.java] = gameMovedToStartingStageEventHandler
        eventHandlerMap[GameStartedEvent::class.java] = EventHandler<GameStartedEvent> {
            val timer = Timer()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    gameStartedEventHandler.handle(it)
                }
            }
            timer.schedule(timerTask, 10000)
        }
        eventHandlerMap[GameTablesCreatedAndPlayersAssociatedEvent::class.java] = NOOP
        eventHandlerMap[NewHandIsClearedToStartEvent::class.java] = NOOP
        eventHandlerMap[PlayerBustedGameEvent::class.java] = playerBustedGameEventHandler
        eventHandlerMap[PlayerMovedToNewTableEvent::class.java] = NOOP
        eventHandlerMap[TablePausedForBalancingEvent::class.java] = NOOP
        eventHandlerMap[TableRemovedEvent::class.java] = NOOP
        eventHandlerMap[TableResumedAfterBalancingEvent::class.java] = NOOP
        return eventHandlerMap
    }

}