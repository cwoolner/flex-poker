package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.repository.GameEventRepository
import com.flexpoker.table.command.commands.ExpireActionOnTimerCommand
import com.flexpoker.table.command.commands.TableCommand
import com.flexpoker.table.command.commands.TickActionOnTimerCommand
import com.flexpoker.table.command.events.ActionOnChangedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.HashMap
import java.util.UUID
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Component
class ActionOnCountdownProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>,
    private val gameEventRepository: GameEventRepository
) : ProcessManager<ActionOnChangedEvent> {

    private val actionOnPlayerScheduledFutureMap: MutableMap<UUID, ScheduledFuture<*>> = HashMap()
    private val scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(16)

    init {
        scheduledThreadPoolExecutor.removeOnCancelPolicy = true
    }

    @Async
    override fun handle(event: ActionOnChangedEvent) {
        clearExistingTimer(event.aggregateId)
        addNewActionOnTimer(event)
    }

    private fun clearExistingTimer(tableId: UUID) {
        val scheduledFuture = actionOnPlayerScheduledFutureMap[tableId]
        scheduledFuture?.cancel(true)
        actionOnPlayerScheduledFutureMap.remove(tableId)
    }

    private fun addNewActionOnTimer(event: ActionOnChangedEvent) {
        val gameCreatedEvent = gameEventRepository.fetchGameCreatedEvent(event.gameId)
        val numberOfSecondsForActionOnTimer = gameCreatedEvent.numberOfSecondsForActionOnTimer
        val scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(
            ActionOnCounter(event, numberOfSecondsForActionOnTimer), 0, 1,
            TimeUnit.SECONDS
        )
        actionOnPlayerScheduledFutureMap[event.aggregateId] = scheduledFuture
    }

    private inner class ActionOnCounter(event: ActionOnChangedEvent, numberOfSecondsForActionOnTimer: Int) : Runnable {
        private var runCount = 0
        private val gameId: UUID = event.gameId
        private val tableId: UUID = event.aggregateId
        private val handId: UUID = event.handId
        private val playerId: UUID = event.playerId
        private val numberOfSecondsForActionOnTimer: Int = numberOfSecondsForActionOnTimer

        override fun run() {
            if (runCount == numberOfSecondsForActionOnTimer) {
                clearExistingTimer(handId)
                tableCommandSender.send(ExpireActionOnTimerCommand(tableId, gameId, handId, playerId))
            } else {
                runCount++
                tableCommandSender.send(TickActionOnTimerCommand(tableId, gameId, handId,
                    numberOfSecondsForActionOnTimer - runCount)
                )
            }
        }

    }

}