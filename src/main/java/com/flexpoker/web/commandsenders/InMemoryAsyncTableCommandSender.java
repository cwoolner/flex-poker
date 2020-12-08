package com.flexpoker.web.commandsenders;

import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.table.command.framework.TableCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class InMemoryAsyncTableCommandSender implements CommandSender<TableCommand> {

    private final CommandReceiver<TableCommand> tableCommandReceiver;

    @Lazy
    @Inject
    public InMemoryAsyncTableCommandSender(
            @Qualifier("tableCommandReceiver")
            CommandReceiver<TableCommand> tableCommandReceiver) {
        this.tableCommandReceiver = tableCommandReceiver;
    }

    @Override
    public void send(TableCommand command) {
        tableCommandReceiver.receive(command);
    }

}
