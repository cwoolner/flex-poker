package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.table.command.commands.StartNewHandForExistingTableCommand
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class StartNewHandForExistingTableProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<NewHandIsClearedToStartEvent> {

    @Async
    override fun handle(event: NewHandIsClearedToStartEvent) {
        val command = StartNewHandForExistingTableCommand(event.tableId, event.aggregateId,
            event.blinds.smallBlind, event.blinds.bigBlind)
        tableCommandSender.send(command)
    }

}