package com.flexpoker.table.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.repository.TableEventRepository;

@Component
public class CreateTableCommandHandler implements CommandHandler<CreateTableCommand> {

    private final TableFactory tableFactory;

    private final EventPublisher<TableEvent> eventPublisher;

    private final TableEventRepository tableEventRepository;

    @Inject
    public CreateTableCommandHandler(TableFactory tableFactory, EventPublisher<TableEvent> eventPublisher,
            TableEventRepository tableEventRepository) {
        this.tableFactory = tableFactory;
        this.eventPublisher = eventPublisher;
        this.tableEventRepository = tableEventRepository;
    }

    @Async
    @Override
    public void handle(CreateTableCommand command) {
        var table = tableFactory.createNew(command);
        var newEvents = table.fetchNewEvents();
        var newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(0, newEvents);
        newlySavedEventsWithVersions.forEach(x -> eventPublisher.publish(x));
    }

}
