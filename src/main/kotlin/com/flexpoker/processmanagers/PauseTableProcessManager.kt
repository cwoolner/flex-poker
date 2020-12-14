package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.TablePausedForBalancingEvent
import com.flexpoker.table.command.commands.PauseCommand
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PauseTableProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<TablePausedForBalancingEvent> {

    @Async
    override fun handle(event: TablePausedForBalancingEvent) {
        val command = PauseCommand(event.tableId, event.aggregateId)
        tableCommandSender.send(command)
    }

}