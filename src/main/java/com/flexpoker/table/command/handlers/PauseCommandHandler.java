package com.flexpoker.table.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.table.command.commands.PauseCommand;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.repository.TableEventRepository;

@Component
public class PauseCommandHandler implements CommandHandler<PauseCommand> {

    private final TableFactory tableFactory;

    private final EventPublisher<TableEvent> eventPublisher;

    private final TableEventRepository tableEventRepository;

    @Inject
    public PauseCommandHandler(TableFactory tableFactory,
            EventPublisher<TableEvent> eventPublisher,
            TableEventRepository tableEventRepository) {
        this.tableFactory = tableFactory;
        this.eventPublisher = eventPublisher;
        this.tableEventRepository = tableEventRepository;
    }

    @Async
    @Override
    public void handle(PauseCommand command) {
        var existingEvents = tableEventRepository.fetchAll(command.getTableId());
        var table = tableFactory.createFrom(existingEvents);
        table.pause();
        var newEvents = table.fetchNewEvents();
        var newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(existingEvents.size(),
                newEvents);
        newlySavedEventsWithVersions.forEach(x -> eventPublisher.publish(x));
    }

}
