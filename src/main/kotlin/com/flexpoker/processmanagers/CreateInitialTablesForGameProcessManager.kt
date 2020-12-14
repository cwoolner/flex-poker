package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class CreateInitialTablesForGameProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> {

    @Async
    override fun handle(event: GameTablesCreatedAndPlayersAssociatedEvent) {
        event.tableIdToPlayerIdsMap.keys.forEach {
            val command = CreateTableCommand(
                it, event.aggregateId, event.tableIdToPlayerIdsMap[it]!!, event.numberOfPlayersPerTable)
            tableCommandSender.send(command)
        }
    }

}