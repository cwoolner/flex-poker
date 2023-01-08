package com.flexpoker.table.command.eventpublishers

import com.flexpoker.framework.event.EventHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.framework.event.subscriber.EventSubscriber
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.TableEvent
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class InMemoryAsyncTableEventPublisher @Inject constructor(
    private val tableEventSubscriber: EventSubscriber<TableEvent>,
    private val actionOnCountdownProcessManager: ProcessManager<ActionOnChangedEvent>,
    private val attemptToStartNewHandForExistingTableProcessManager: ProcessManager<HandCompletedEvent>,
    private val autoMoveHandForwardProcessManager: ProcessManager<AutoMoveHandForwardEvent>
) : EventPublisher<TableEvent> {

    companion object {
        private val NOOP = EventHandler { _: TableEvent -> }
    }

    override fun publish(event: TableEvent) {
        tableEventSubscriber.receive(event)
        when (event) {
            is ActionOnChangedEvent -> actionOnCountdownProcessManager.handle(event)
            is HandCompletedEvent -> attemptToStartNewHandForExistingTableProcessManager.handle(event)
            is AutoMoveHandForwardEvent -> autoMoveHandForwardProcessManager.handle(event)
            else -> NOOP
        }
    }

}