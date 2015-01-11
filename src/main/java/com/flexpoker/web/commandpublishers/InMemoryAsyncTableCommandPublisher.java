package com.flexpoker.web.commandpublishers;

import javax.inject.Inject;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.framework.command.CommandSubscriber;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class InMemoryAsyncTableCommandPublisher implements
        CommandPublisher<TableCommandType> {

    private final CommandSubscriber<TableCommandType> tableCommandSubscriber;

    @Lazy
    @Inject
    public InMemoryAsyncTableCommandPublisher(
            CommandSubscriber<TableCommandType> tableCommandSubscriber) {
        this.tableCommandSubscriber = tableCommandSubscriber;
    }

    @Override
    public void publish(Command<TableCommandType> command) {
        tableCommandSubscriber.receive(command);
    }

}
