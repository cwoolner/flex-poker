package com.flexpoker.table.command.publish;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class InMemoryAsyncTableCommandPublisher implements
        CommandPublisher<TableCommandType> {

    // @Inject
    public InMemoryAsyncTableCommandPublisher() {
    }

    @Override
    public void publish(Command<TableCommandType> command) {
        switch (command.getType()) {
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
