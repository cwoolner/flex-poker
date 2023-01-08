package com.flexpoker.game.command.eventpublishers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.framework.event.subscriber.EventSubscriber
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import org.springframework.stereotype.Component
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@Component
class InMemoryAsyncGameEventPublisher @Inject constructor(
    private val gameEventSubscriber: EventSubscriber<GameEvent>,
    private val createInitialTablesForGameProcessManager: ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent>,
    private val startFirstHandProcessManager: ProcessManager<GameStartedEvent>,
    private val startNewHandForExistingTableProcessManager: ProcessManager<NewHandIsClearedToStartEvent>,
    private val incrementBlindsCountdownProcessManager: ProcessManager<GameStartedEvent>,
    private val movePlayerBetweenTablesProcessManager: ProcessManager<PlayerMovedToNewTableEvent>,
    private val pauseTableProcessManager: ProcessManager<TablePausedForBalancingEvent>,
    private val resumeTableProcessManager: ProcessManager<TableResumedAfterBalancingEvent>
) : EventPublisher<GameEvent> {

    companion object {
        private val NOOP = EventHandler { _: GameEvent -> }
    }

    override fun publish(event: GameEvent) {
        gameEventSubscriber.receive(event)
        when (event) {
            is GameTablesCreatedAndPlayersAssociatedEvent -> createInitialTablesForGameProcessManager.handle(event)
            is GameStartedEvent -> {
                val localStartFirstHandProcessManager = startFirstHandProcessManager
                val timer = Timer()
                val timerTask: TimerTask = object : TimerTask() {
                    override fun run() {
                        localStartFirstHandProcessManager.handle(event)
                    }
                }
                timer.schedule(timerTask, 10000)
                incrementBlindsCountdownProcessManager.handle(event)
            }
            is NewHandIsClearedToStartEvent -> startNewHandForExistingTableProcessManager.handle(event)
            is PlayerMovedToNewTableEvent -> movePlayerBetweenTablesProcessManager.handle(event)
            is TablePausedForBalancingEvent -> pauseTableProcessManager.handle(event)
            is TableResumedAfterBalancingEvent -> resumeTableProcessManager.handle(event)
            else -> NOOP
        }
    }

}