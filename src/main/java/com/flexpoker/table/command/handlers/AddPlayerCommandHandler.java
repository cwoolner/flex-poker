package com.flexpoker.table.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.table.command.commands.AddPlayerCommand;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.repository.TableEventRepository;

@Component
public class AddPlayerCommandHandler
        implements CommandHandler<AddPlayerCommand> {

    private final TableFactory tableFactory;

    private final EventPublisher<TableEvent> eventPublisher;

    private final TableEventRepository tableEventRepository;

    @Inject
    public AddPlayerCommandHandler(TableFactory tableFactory,
            EventPublisher<TableEvent> eventPublisher,
            TableEventRepository tableEventRepository) {
        this.tableFactory = tableFactory;
        this.eventPublisher = eventPublisher;
        this.tableEventRepository = tableEventRepository;
    }

    @Async
    @Override
    public void handle(AddPlayerCommand command) {
        var tableEvents = tableEventRepository.fetchAll(command.getTableId());
        var table = tableFactory.createFrom(tableEvents);

        table.addPlayer(command.getPlayerId(), command.getChips());
        table.fetchNewEvents().forEach(x -> tableEventRepository.save(x));
        table.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }

}
