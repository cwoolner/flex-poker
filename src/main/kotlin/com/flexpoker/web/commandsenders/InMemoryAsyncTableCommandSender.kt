package com.flexpoker.web.commandsenders

import com.flexpoker.framework.command.CommandReceiver
import com.flexpoker.framework.command.CommandSender
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class InMemoryAsyncTableCommandSender @Lazy @Inject constructor(
    private val tableCommandReceiver: CommandReceiver<TableCommand>
) : CommandSender<TableCommand> {

    override fun send(command: TableCommand) {
        tableCommandReceiver.receive(command)
    }

}