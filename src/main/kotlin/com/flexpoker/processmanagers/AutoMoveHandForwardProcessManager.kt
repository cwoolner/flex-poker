package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.table.command.commands.AutoMoveHandForwardCommand
import com.flexpoker.table.command.commands.TableCommand
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Component
class AutoMoveHandForwardProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<AutoMoveHandForwardEvent> {

    private val scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(16)

    @Async
    override fun handle(event: AutoMoveHandForwardEvent) {
        scheduledThreadPoolExecutor.schedule({
            val command = AutoMoveHandForwardCommand(event.aggregateId, event.gameId)
            tableCommandSender.send(command)
        }, 2, TimeUnit.SECONDS)
    }

}