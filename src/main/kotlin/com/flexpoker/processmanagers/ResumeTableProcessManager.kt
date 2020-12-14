package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent
import com.flexpoker.table.command.commands.ResumeCommand
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class ResumeTableProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<TableResumedAfterBalancingEvent> {

    @Async
    override fun handle(event: TableResumedAfterBalancingEvent) {
        val command = ResumeCommand(event.tableId, event.aggregateId)
        tableCommandSender.send(command)
    }

}