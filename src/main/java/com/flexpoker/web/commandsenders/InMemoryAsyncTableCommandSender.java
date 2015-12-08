package com.flexpoker.web.commandsenders;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class InMemoryAsyncTableCommandSender
        implements CommandSender<TableCommandType> {

    private final CommandReceiver<TableCommandType> tableCommandReceiver;

    @Lazy
    @Inject
    public InMemoryAsyncTableCommandSender(
            @Qualifier("tableCommandReceiver")
            CommandReceiver<TableCommandType> tableCommandReceiver) {
        this.tableCommandReceiver = tableCommandReceiver;
    }

    @Override
    public void send(Command<TableCommandType> command) {
        tableCommandReceiver.receive(command);
    }

}
